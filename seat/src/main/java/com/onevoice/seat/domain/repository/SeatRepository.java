package com.onevoice.seat.domain.repository;


import com.onevoice.seat.domain.Seat;
import com.onevoice.seat.domain.vo.SeatCode;
import com.onevoice.seat.domain.vo.SessionId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository {
    List<Seat> findAllBySessionId(SessionId sessionId);

    Optional<Seat> findBySessionIdAndSeatCode(SessionId sessionId, SeatCode seatCode);

    void saveAll(List<Seat> seats);
    void deleteAllBySessionId(SessionId sessionId);
    List<Seat> findBySeatIdIn(List<UUID> seatIds);

}