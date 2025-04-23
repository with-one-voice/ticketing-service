package com.onevoice.seat.application.service;

import com.onevoice.common.enumtype.KafkaTopicType;
import com.onevoice.common.enumtype.SeatStatus;
import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.application.dto.HoldSeatCommand;
import com.onevoice.seat.application.dto.message.SeatConfirmedMessage;
import com.onevoice.seat.application.dto.message.SeatFailedMessage;
import com.onevoice.seat.application.event.GenericKafkaEvent;
import com.onevoice.seat.domain.Seat;
import com.onevoice.seat.domain.repository.SeatRepository;
import com.onevoice.seat.domain.vo.Money;
import com.onevoice.seat.domain.vo.SeatCode;
import com.onevoice.seat.domain.vo.SessionId;
import com.onevoice.seat.exception.SeatAlreadyHeldException;
import com.onevoice.seat.exception.SeatNotFoundException;
import com.onevoice.seat.infrastructure.message.SeatEventProducer;
import com.onevoice.seat.infrastructure.redis.RedisKeyUtil;
import com.onevoice.seat.presentation.dto.response.HoldSeatResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatCreateResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatResponseDto;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, Object> objectRedisTemplate;
    private final RedissonClient redissonClient;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SeatEventProducer seatEventProducer;

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

        try {
            seatRepository.saveAll(seats);

            redisTemplate.opsForHash().putAll(
                RedisKeyUtil.seatStatusKey(sessionId.getValue()),
                seats.stream().collect(Collectors.toMap(
                    s -> s.getSeatId().toString(),
                    s -> s.getStatus().name()
                ))
            );
            // 성공 시 -> Show 로 성공 메시지 발행
            seatEventProducer.sendCreateSuccess(command.sessionId());

        } catch (Exception ex) {
            // 실패 시 -> Show 로 실패 메시지 발행
            seatEventProducer.sendCreateFail(command.sessionId());
        }

        return seats.stream().map(SeatCreateResponseDto::of).toList();
    }


    /*
     * 좌석 단건 조회
     * */
    @Override
    public SeatResponseDto getSeat(UUID sessionId, UUID seatId) {

        Seat seat = seatRepository.findById(seatId)
            .orElseThrow(SeatNotFoundException::new);

        String redisKey = RedisKeyUtil.seatStatusKey(sessionId);
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
        String cacheKey = RedisKeyUtil.seatCacheKey(sessionId);

        // 1. 캐시 조회 시도
        List<SeatResponseDto> cached = (List<SeatResponseDto>) objectRedisTemplate.opsForValue()
            .get(cacheKey);
        if (cached != null) {
            log.info(">>> Redis 캐시에서 회차별 좌석 조회 결과 반환");
            return cached;
        }

        // 2. DB 및 Redis 선점 상태 조회
        SessionId session = new SessionId(sessionId);
        List<Seat> seats = seatRepository.findAllBySessionId(session);
        String redisKey = RedisKeyUtil.seatStatusKey(sessionId);
        Map<Object, Object> redisStatuses = redisTemplate.opsForHash().entries(redisKey);

        List<SeatResponseDto> response = seats.stream()
            .map(seat -> {
                String seatId = seat.getSeatId().toString();
                if (redisStatuses.containsKey(seatId)) {
                    SeatStatus redisStatus = SeatStatus.valueOf((String) redisStatuses.get(seatId));
                    return SeatResponseDto.of(seat.withStatus(redisStatus));
                }
                return SeatResponseDto.of(seat);
            }).toList();

        // 3. 캐시에 저장 (TTL 1분)
        objectRedisTemplate.opsForValue().set(cacheKey, response, Duration.ofMinutes(1));

        return response;
    }

    //좌석 선점
    public HoldSeatResponseDto holdSeat(HoldSeatCommand command) {
        SessionId sessionId = new SessionId(command.sessionId());
        List<UUID> seatIdList = command.seatIds();
        UUID userId = command.userId();
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(5);

        //좌석 조회
        List<Seat> seats = seatIdList.stream()
            .map(id -> seatRepository.findById(id).orElseThrow(SeatNotFoundException::new))
            .toList();

        String redisKey = RedisKeyUtil.seatStatusKey(sessionId.getValue());

        //상태 확인 및 Redis TTL (1분) 설정
        for (Seat seat : seats) {
            String seatIdStr = seat.getSeatId().toString();
            String holdKey = RedisKeyUtil.seatHoldKey(sessionId.getValue(), seat.getSeatId());
            String lockKey = RedisKeyUtil.seatLockKey(seat.getSeatId());

            //Redisson 락 생성
            RLock lock = redissonClient.getLock(lockKey);

            try {
                //  락 시도 (2초 대기, 5초 유지)
                log.info("[{}] 락 획득 시도 중... (락 키: {})", seatIdStr, lockKey);
                boolean locked = lock.tryLock(2, 10, TimeUnit.SECONDS);
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
                // 문자열: userId,expireAt 저장 (TTL x)
                String redisValue = userId + "," + expireAt;
                redisTemplate.opsForValue().set(holdKey, redisValue);
                log.info("[{}] 선점 성공 → 상태: HOLD", seatIdStr);
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
        redisTemplate.delete(RedisKeyUtil.seatCacheKey(sessionId.getValue()));
        return HoldSeatResponseDto.success(expireAt, seatIdList);

    }


    @Override
    public void deleteSeat(UUID sessionId) {
        SessionId session = new SessionId(sessionId);
        seatRepository.deleteAllBySessionId(session);
        redisTemplate.delete(RedisKeyUtil.seatStatusKey(sessionId));
        redisTemplate.delete(RedisKeyUtil.seatCacheKey(sessionId));
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
            UUID sessionId = seats.get(0).getSessionId().getValue();
            String redisKey = RedisKeyUtil.seatStatusKey(sessionId);

            for (Seat seat : seats) {
                UUID seatId = seat.getSeatId();
                redisTemplate.opsForHash().put(redisKey, seatId.toString(), newStatus.name());

                if (newStatus == SeatStatus.AVAILABLE) {
                    String holdKey = RedisKeyUtil.seatHoldKey(sessionId, seatId);
                    redisTemplate.delete(holdKey);
                }
            }
            redisTemplate.delete(RedisKeyUtil.seatCacheKey(sessionId));
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

            //유저 정보 저장
            seat.assignUser(userId);

            UUID sessionId = seat.getSessionId().getValue();
            UUID seatId = seat.getSeatId();

            String redisKey = RedisKeyUtil.seatStatusKey(sessionId);
            String holdKey = RedisKeyUtil.seatHoldKey(sessionId, seatId);

            redisTemplate.opsForHash().put(redisKey, seatId.toString(), SeatStatus.RESERVED.name());
            redisTemplate.delete(holdKey);
            redisTemplate.delete(RedisKeyUtil.seatCacheKey(sessionId));

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

            //userId 초기화
            seat.clearUserId();

            UUID sessionId = seat.getSessionId().getValue();
            UUID seatId = seat.getSeatId();

            String redisKey = RedisKeyUtil.seatStatusKey(sessionId);
            String holdKey = RedisKeyUtil.seatHoldKey(sessionId, seatId);

            redisTemplate.opsForHash()
                .put(redisKey, seatId.toString(), SeatStatus.AVAILABLE.name());
            redisTemplate.delete(holdKey);
            redisTemplate.delete(RedisKeyUtil.seatCacheKey(sessionId));
        }

        seatRepository.saveAll(seats);

        // 이벤트 발행
        SeatFailedMessage message = new SeatFailedMessage(userId);
        GenericKafkaEvent<SeatFailedMessage> event = new GenericKafkaEvent<>(
            KafkaTopicType.SEAT_CANCEL.getTopic(), message
        );
        applicationEventPublisher.publishEvent(event);
    }
}
