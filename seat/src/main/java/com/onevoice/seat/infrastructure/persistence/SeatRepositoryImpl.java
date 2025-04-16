package com.onevoice.seat.infrastructure.persistence;

import com.onevoice.seat.domain.QSeat;
import com.onevoice.seat.domain.Seat;
import com.onevoice.seat.domain.repository.SeatRepository;
import com.onevoice.seat.domain.vo.SeatCode;
import com.onevoice.seat.domain.vo.SessionId;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {
    private final SeatJpaRepository seatJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final QSeat qSeat = QSeat.seat;



    @Override
    public List<Seat> findAllBySessionId(SessionId sessionId) {
        return queryFactory.selectFrom(qSeat)
                .where(qSeat.sessionId.eq(sessionId))
                .fetch();
    }

    @Override
    public void saveAll(List<Seat> seats) {
        seatJpaRepository.saveAll(seats);
    }
    @Override
    public void deleteAllBySessionId(SessionId sessionId) {
        List<Seat> seats = seatJpaRepository.findBySessionId(sessionId);
        seatJpaRepository.deleteAll(seats);
    }

    @Override
    public List<Seat> findBySeatIdIn(List<UUID> seatIds) {
        return queryFactory
                .selectFrom(qSeat)
                .where(qSeat.seatId.in(seatIds))
                .fetch();
    }

    @Override
    public Optional<Seat> findById(UUID seatId) {
        return Optional.ofNullable(queryFactory
            .selectFrom(qSeat)
            .where(qSeat.seatId.eq(seatId),
                qSeat.deletedAt.isNull())
            .fetchFirst()
        );
    }
}
