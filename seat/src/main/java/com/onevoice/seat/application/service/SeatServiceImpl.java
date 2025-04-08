package com.onevoice.seat.application.service;

import com.onevoice.seat.application.dto.CreateSeatCommand;
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
}