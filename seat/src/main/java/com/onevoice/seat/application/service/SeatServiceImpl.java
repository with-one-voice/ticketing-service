package com.onevoice.seat.application.service;

import com.onevoice.seat.application.dto.CreateSeatCommand;
import com.onevoice.seat.application.dto.HoldSeatCommand;
import com.onevoice.seat.presentation.dto.response.HoldSeatResponseDto;
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
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.onevoice.seat.domain.QSeat.seat;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<SeatResponseDto> createSeats(CreateSeatCommand command) {
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

        return seats.stream().map(SeatResponseDto::of).toList();
    }


    @Override
    public SeatResponseDto getSeat(UUID sessionId, String seatCode) {


        SessionId session = new SessionId(sessionId);
        SeatCode code = new SeatCode(seatCode);

        Seat seat = seatRepository.findBySessionIdAndSeatCode(session, code)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        return SeatResponseDto.of(seat);
    }

    @Override
    public List<SeatResponseDto> getSeatsBySession(UUID sessionId) {
        SessionId session = new SessionId(sessionId);

        List<Seat> seats = seatRepository.findAllBySessionId(session);
        return seats.stream().map(SeatResponseDto::of).toList();
    }
    //좌석 선점
    public HoldSeatResponseDto holdSeats(HoldSeatCommand command) {
        SessionId sessionId = new SessionId(command.sessionId());
        List<String> seatCodes = command.seatCodes();
        UUID userId = command.userId();

        //1. 좌석 조회
        List<Seat> seats = seatCodes.stream()
                .map(code -> seatRepository.findBySessionIdAndSeatCode(sessionId, new SeatCode(code))
                        .orElseThrow(() -> new RuntimeException("좌석을 찾을 수 없습니다: " + code)))
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
                throw new RuntimeException("이미 선점된 좌석입니다: " + seat.getSeatCode().getValue());
            }

            redisTemplate.opsForHash().put(redisKey, seat.getSeatCode().getValue(), "HOLD");
            redisTemplate.opsForValue().set(holdKey, userId.toString(), Duration.ofMinutes(1));
        }

        return HoldSeatResponseDto.success(LocalDateTime.now().plusMinutes(1), seatIds);
    }

}
