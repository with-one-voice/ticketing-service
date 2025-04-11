package com.onevoice.show.infrastructure;

import com.onevoice.show.domain.QSession;
import com.onevoice.show.domain.QShow;
import com.onevoice.show.domain.Session;
import com.onevoice.show.domain.repository.SessionRepository;
import com.onevoice.show.infrastructure.jpa.SessionJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {

    private final SessionJpaRepository sessionJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Session save(Session session) {
        return sessionJpaRepository.save(session);
    }

    @Override
    public List<Session> findAll() {
        QSession session = QSession.session;
        QShow show = QShow.show;

        return queryFactory
            .selectFrom(session)
            .join(session.show, show).fetchJoin()
            .where(session.deletedAt.isNull())
            .fetch();
    }

    @Override
    public List<Session> findByShowId(UUID showId) {
        QSession session = QSession.session;
        QShow show = QShow.show;

        return queryFactory
            .selectFrom(session)
            .join(session.show, show).fetchJoin()
            .where(
                session.show.id.eq(showId),
                session.deletedAt.isNull()
            )
            .fetch();
    }

    @Override
    public Optional<Session> findById(UUID sessionId) {
        QSession session = QSession.session;
        return Optional.ofNullable(queryFactory
            .selectFrom(session)
            .where(session.id.eq(sessionId)
                .and(session.deletedAt.isNull())
            )
            .fetchFirst()
        );
    }

    @Override
    public Optional<Session> find(UUID showId, LocalDate sessionDate) {
        QSession session = QSession.session;
        QShow show = QShow.show;

        return Optional.ofNullable(queryFactory
            .selectFrom(session)
            .join(session.show, show).fetchJoin()
            .where(
                session.show.id.eq(showId),
                session.sessionDate.eq(sessionDate),
                session.deletedAt.isNull()
            )
            .fetchFirst()
        );
    }
}
