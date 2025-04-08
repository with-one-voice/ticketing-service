package com.onevoice.seat.domain.repository;


import com.onevoice.seat.domain.Seat;
import com.onevoice.seat.domain.vo.SeatCode;
import com.onevoice.seat.domain.vo.SessionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, String> {
    List<Seat> findBySessionId(SessionId sessionId);
    Optional<Seat> findBySessionIdAndSeatCode(SessionId sessionId, SeatCode seatCode);
}