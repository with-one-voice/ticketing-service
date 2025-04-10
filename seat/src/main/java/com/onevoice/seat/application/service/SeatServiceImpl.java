package com.onevoice.seat.application.service;

import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.application.dto.HoldSeatCommand;
import com.onevoice.seat.exception.SeatAlreadyHeldException;
import com.onevoice.seat.exception.SeatNotFoundException;
import com.onevoice.seat.presentation.dto.response.HoldSeatResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatCreateResponseDto;
import com.onevoice.seat.presentation.dto.response.SeatResponseDto;
import com.onevoice.seat.domain.Seat;
import com.onevoice.seat.domain.SeatStatus;
import com.onevoice.seat.domain.repository.SeatRepository;
import com.onevoice.seat.domain.vo.Money;
import com.onevoice.seat.domain.vo.SeatCode;
import com.onevoice.seat.domain.vo.SessionId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
@Transactional
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final RedisTemplate<String, String> redisTemplate;


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
                        s -> s.getSeatCode().getValue(),
                        s -> s.getStatus().name()
                ))
        );

        return seats.stream().map(SeatCreateResponseDto::of).toList();
    }


    /*
    * 좌석 단건 조회
    * */
    @Override
    public SeatResponseDto getSeat(UUID sessionId, String seatCode) {


        SessionId session = new SessionId(sessionId);
        SeatCode code = new SeatCode(seatCode);

        Seat seat = seatRepository.findBySessionIdAndSeatCode(session, code)
                .orElseThrow(SeatNotFoundException::new);
        //  Redis에서 상태 조회
        String redisKey = "seat:" + sessionId;
        String redisStatus = (String) redisTemplate.opsForHash()
                .get(redisKey, seatCode);

        //  Redis에 상태가 있으면 그걸로 덮어쓰기
        if (redisStatus != null) {
            SeatStatus redisSeatStatus = SeatStatus.valueOf(redisStatus);
            seat = seat.withStatus(redisSeatStatus);  // 새로운 상태 적용
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
                    String seatCode = seat.getSeatCode().getValue();
                    if (redisStatuses.containsKey(seatCode)) {
                        SeatStatus redisStatus = SeatStatus.valueOf((String) redisStatuses.get(seatCode));
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
        List<String> seatCodes = command.seatCodes();
        UUID userId = command.userId();

        //좌석 조회
        List<Seat> seats = seatCodes.stream()
                .map(code -> seatRepository.findBySessionIdAndSeatCode(sessionId, new SeatCode(code))
                        .orElseThrow(SeatNotFoundException::new))
                .toList();

        List<UUID> seatIds = seats.stream()
                .map(Seat::getSeatId)
                .toList();

        //상태 확인 및 Redis TTL (1분) 설정
        for (Seat seat : seats) {
            String redisKey = "seat:" + sessionId.getValue();
            String holdKey = "seat-hold:" + sessionId.getValue() + ":" + seat.getSeatCode().getValue();

            String currentStatus = (String) redisTemplate.opsForHash()
                    .get(redisKey, seat.getSeatCode().getValue());

            if (!"AVAILABLE".equals(currentStatus)) {
                throw new SeatAlreadyHeldException();
            }

            redisTemplate.opsForHash().put(redisKey, seat.getSeatCode().getValue(), "HOLD");
            redisTemplate.opsForValue().set(holdKey, userId.toString(), Duration.ofMinutes(5));
        }

        return HoldSeatResponseDto.success(LocalDateTime.now().plusMinutes(5), seatIds);
    }

    @Override
    public void deleteSeat(UUID sessionId){
        SessionId session = new SessionId(sessionId);
        seatRepository.deleteAllBySessionId(session);
        redisTemplate.delete("seat:" + sessionId.toString());
    }

}
