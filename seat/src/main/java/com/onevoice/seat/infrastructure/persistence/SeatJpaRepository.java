package com.onevoice.seat.infrastructure.persistence;

import com.onevoice.seat.domain.Seat;
import com.onevoice.seat.domain.vo.SessionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeatJpaRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findBySessionId(SessionId sessionId);
}
