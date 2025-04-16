package com.onevoice.seat.application.service;

import com.onevoice.common.enumtype.KafkaTopicType;
import com.onevoice.common.enumtype.SeatStatus;
import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.application.dto.HoldSeatCommand;
import com.onevoice.seat.application.dto.message.SeatConfirmedMessage;
import com.onevoice.seat.application.dto.message.SeatFailedMessage;
import com.onevoice.seat.application.event.GenericKafkaEvent;
import com.onevoice.seat.exception.SeatAlreadyHeldException;
import com.onevoice.seat.exception.SeatNotFoundException;
import com.onevoice.seat.presentation.dto.response.HoldSeatResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatCreateResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatResponseDto;
import com.onevoice.seat.domain.Seat;
import com.onevoice.seat.domain.repository.SeatRepository;
import com.onevoice.seat.domain.vo.Money;
import com.onevoice.seat.domain.vo.SeatCode;
import com.onevoice.seat.domain.vo.SessionId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedissonClient redissonClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    /*
    * 좌석 생성
    * */
    @Override
    public List<SeatCreateResponseDto> createSeat(CreateSeatCommand command) {
        SessionId sessionId = new SessionId(command.sessionId());
        Money price = new Money(command.price());

        List<Seat> seats = IntStream.rangeClosed(1, command.seatCount())
                .mapToObj(i -> {
                    String code = "S" + i;
                    return new Seat(new SeatCode(code), sessionId, SeatStatus.AVAILABLE, price);
                }).toList();

        seatRepository.saveAll(seats);

        redisTemplate.opsForHash().putAll("seat:" + sessionId.getValue(),
                seats.stream().collect(Collectors.toMap(
                        s -> s.getSeatId().toString(),
                        s -> s.getStatus().name()
                ))
        );

        return seats.stream().map(SeatCreateResponseDto::of).toList();
    }


    /*
    * 좌석 단건 조회
    * */
    @Override
    public SeatResponseDto getSeat(UUID sessionId, UUID seatId) {

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(SeatNotFoundException::new);

        String redisKey = "seat:" + sessionId;
        String redisStatus = (String) redisTemplate.opsForHash()
                .get(redisKey, seatId.toString());

        if (redisStatus != null) {
            SeatStatus redisSeatStatus = SeatStatus.valueOf(redisStatus);
            seat = seat.withStatus(redisSeatStatus); //새로운 상태 적용
        }

        return SeatResponseDto.of(seat);
    }

    //해당 회차별 좌석 목록 조회
    @Override
    public List<SeatResponseDto> getSeatBySession(UUID sessionId) {
        SessionId session = new SessionId(sessionId);
        List<Seat> seats = seatRepository.findAllBySessionId(session);
        //  Redis에서 전체 좌석 상태 가져오기
        String redisKey = "seat:" + sessionId;
        Map<Object, Object> redisStatuses = redisTemplate.opsForHash().entries(redisKey);

        //  Redis 상태를 반영해서 Seat 객체 상태 수정
        List<SeatResponseDto> response = seats.stream()
                .map(seat -> {
                    String seatId = seat.getSeatId().toString();
                    if (redisStatuses.containsKey(seatId)) {
                        SeatStatus redisStatus = SeatStatus.valueOf((String) redisStatuses.get(seatId));
                        return SeatResponseDto.of(seat.withStatus(redisStatus));
                    }
                    return SeatResponseDto.of(seat);
                })
                .toList();

        return response;
    }
    //좌석 선점
    public HoldSeatResponseDto holdSeat(HoldSeatCommand command) {
        SessionId sessionId = new SessionId(command.sessionId());
        List<UUID> seatIdList = command.seatIds();
        UUID userId = command.userId();

        //좌석 조회
        List<Seat> seats = seatIdList.stream()
                .map(id -> seatRepository.findById(id).orElseThrow(SeatNotFoundException::new))
                .toList();

        String redisKey = "seat:" + sessionId.getValue();

        //상태 확인 및 Redis TTL (1분) 설정
        for (Seat seat : seats) {
            String seatIdStr = seat.getSeatId().toString();
            String holdKey = "seat-hold:" + sessionId.getValue() + ":" + seatIdStr;
            String lockKey = "lock:seat:" + seatIdStr;

            //Redisson 락 생성
            RLock lock = redissonClient.getLock(lockKey);

            try {
                //  락 시도 (2초 대기, 5초 유지)
                log.info("[{}] 락 획득 시도 중... (락 키: {})", seatIdStr, lockKey);
                boolean locked = lock.tryLock(2, 60, TimeUnit.SECONDS);
                if (!locked) {
                    log.warn("[{}] 락 획득 실패 - 이미 다른 사용자가 점유 중", seatIdStr);
                    throw new SeatAlreadyHeldException(); // 락 획득 실패
                }
                log.info("[{}] 락 획득 성공!", seatIdStr);
                String currentStatus = (String) redisTemplate.opsForHash().get(redisKey, seatIdStr);
                log.info("[{}] 현재 Redis 상태: {}", seatIdStr, currentStatus);

                if (!"AVAILABLE".equals(currentStatus)) {
                    log.warn("[{}] 현재 상태가 AVAILABLE이 아니므로 선점 불가", seatIdStr);
                    throw new SeatAlreadyHeldException();
                }

                redisTemplate.opsForHash().put(redisKey, seatIdStr, "HOLD");
                redisTemplate.opsForValue().set(holdKey, userId.toString(), Duration.ofMinutes(5));
                log.info("[{}] 선점 성공 → 상태: HOLD, TTL 5분 설정 완료", seatIdStr);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("[{}] 락 대기 중 인터럽트 발생", seatIdStr, e);
                throw new RuntimeException("Seat lock interrupted", e);
            } finally {
                //  현재 쓰레드가 락 소유 중일 때만 해제
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    log.info("[{}] 락 해제 완료", seatIdStr);
                }
                }
            }

//            String currentStatus = (String) redisTemplate.opsForHash().get(redisKey, seatIdStr);
//
//            if (!"AVAILABLE".equals(currentStatus)) {
//                throw new SeatAlreadyHeldException();
//            }
//
//            redisTemplate.opsForHash().put(redisKey, seatIdStr, "HOLD");
//            redisTemplate.opsForValue().set(holdKey, userId.toString(), Duration.ofMinutes(5));
//        }

        return HoldSeatResponseDto.success(LocalDateTime.now().plusMinutes(10), seatIdList);
    }



    @Override
    public void deleteSeat(UUID sessionId){
        SessionId session = new SessionId(sessionId);
        seatRepository.deleteAllBySessionId(session);
        redisTemplate.delete("seat:" + sessionId.toString());
    }


    /*
    * 좌석 상태 변경
    * */
    @Override
    public List<SeatResponseDto> updateSeatStatuses(List<UUID> seatIds, SeatStatus newStatus) {
        List<Seat> seats = seatRepository.findBySeatIdIn(seatIds);

        for (Seat seat : seats) {
            seat.changeStatus(newStatus);
        }

        seatRepository.saveAll(seats);

        if (!seats.isEmpty()) {
            String redisKey = "seat:" + seats.get(0).getSessionId().getValue();
            for (Seat seat : seats) {
                redisTemplate.opsForHash().put(redisKey, seat.getSeatId().toString(), newStatus.name());
                //상태 AVALIABLE면 holdKey도 삭제
                if (newStatus == SeatStatus.AVAILABLE) {  //
                    String holdKey = "seat-hold:" + seat.getSessionId().getValue() + ":" + seat.getSeatId();
                    redisTemplate.delete(holdKey);
                }

            }
        }

        return seats.stream().map(SeatResponseDto::of).toList();
    }


    /*
    * Kafka 메시지 수신 - 예매 확정 처리
    * */
    @Override
    public void confirmSeats(List<UUID> seatIds, UUID userId) {
        List<Seat> seats = seatRepository.findBySeatIdIn(seatIds);

        for (Seat seat : seats) {
            seat.changeStatus(SeatStatus.RESERVED);


            String redisKey = "seat:" + seat.getSessionId().getValue();
            String seatIdStr = seat.getSeatId().toString();

            redisTemplate.opsForHash().put(
                    redisKey,
                    seatIdStr,
                    SeatStatus.RESERVED.name()
            );

            // TTL 기반 hold 키 삭제
            String holdKey = "seat-hold:" + seat.getSessionId().getValue() + ":" + seatIdStr;
            redisTemplate.delete(holdKey);
        }

        seatRepository.saveAll(seats);

        // 이벤트 발행
        SeatConfirmedMessage message = new SeatConfirmedMessage(userId);
        GenericKafkaEvent<SeatConfirmedMessage> event = new GenericKafkaEvent<>(
                KafkaTopicType.SEAT_CONFIRM.getTopic(), message
        );
        applicationEventPublisher.publishEvent(event);
    }

    /*
    * Kafka 메시지 수신 - 예매 실패 복구
    * */
    @Override
    public void revertSeats(List<UUID> seatIds, UUID userId) {
        List<Seat> seats = seatRepository.findBySeatIdIn(seatIds);

        for (Seat seat : seats) {
            seat.changeStatus(SeatStatus.AVAILABLE);


            String redisKey = "seat:" + seat.getSessionId().getValue();
            String seatIdStr = seat.getSeatId().toString();

            redisTemplate.opsForHash().put(
                    redisKey,
                    seatIdStr,
                    SeatStatus.AVAILABLE.name()
            );

            // TTL 기반 hold 키 삭제
            String holdKey = "seat-hold:" + seat.getSessionId().getValue() + ":" + seatIdStr;
            redisTemplate.delete(holdKey);
        }

        seatRepository.saveAll(seats);

        // 이벤트 발행
        SeatFailedMessage message = new SeatFailedMessage(userId);
        GenericKafkaEvent<SeatFailedMessage> event = new GenericKafkaEvent<>(
                KafkaTopicType.SEAT_CONFIRM_FAIL.getTopic(), message
        );
        applicationEventPublisher.publishEvent(event);
    }
}
