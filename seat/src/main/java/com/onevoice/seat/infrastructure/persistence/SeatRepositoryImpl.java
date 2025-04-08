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

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {
    private final SeatJpaRepository seatJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final QSeat qSeat = QSeat.seat;

    @Override
    public Seat save(Seat seat) {
        return seatJpaRepository.save(seat);
    }

    @Override
    public List<Seat> findBySessionId(SessionId sessionId) {
        return List.of();
    }

    @Override
    public List<Seat> findAllBySessionId(SessionId sessionId) {
        return queryFactory.selectFrom(qSeat)
                .where(qSeat.sessionId.eq(sessionId))
                .fetch();
    }

    @Override
    public Optional<Seat> findBySessionIdAndSeatCode(SessionId sessionId, SeatCode seatCode) {
        return Optional.ofNullable(
                queryFactory.selectFrom(qSeat)
                        .where(
                                qSeat.sessionId.eq(sessionId),
                                qSeat.seatCode.eq(seatCode)
                        )
                        .fetchOne()
        );
    }

    @Override
    public void saveAll(List<Seat> seats) {
        seatJpaRepository.saveAll(seats);
    }
}
