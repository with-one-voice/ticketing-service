package com.onevoice.show.infrastructure;

import com.onevoice.show.domain.Session;
import com.onevoice.show.domain.repository.SessionRepository;
import com.onevoice.show.infrastructure.jpa.SessionJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
}
