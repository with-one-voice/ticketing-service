package com.onevoice.seat.domain.repository;


import com.onevoice.seat.domain.Seat;
import com.onevoice.seat.domain.vo.SeatCode;
import com.onevoice.seat.domain.vo.SessionId;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    Seat save(Seat seat);;
    List<Seat> findBySessionId(SessionId sessionId);

    List<Seat> findAllBySessionId(SessionId sessionId);

    Optional<Seat> findBySessionIdAndSeatCode(SessionId sessionId, SeatCode seatCode);

    void saveAll(List<Seat> seats);
    void deleteAllBySessionId(SessionId sessionId);
}