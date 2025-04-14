package com.onevoice.user.infrastructure;

import com.onevoice.user.domain.QUser;
import com.onevoice.user.domain.User;
import com.onevoice.user.domain.repository.UserRepository;
import com.onevoice.user.infrastructure.jpa.UserJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findByEmail(String email) {
        QUser user = QUser.user;
        return Optional.ofNullable(queryFactory
            .selectFrom(user)
            .where(user.email.value.eq(email))
            .fetchFirst());
    }

    @Override
    public Optional<User> findById(UUID userId) {
        QUser user = QUser.user;
        return Optional.ofNullable(queryFactory
            .selectFrom(user)
            .where(user.id.eq(userId))
            .fetchFirst());
    }

    @Override
    public User save(User user) {
        return jpaRepository.save(user);
    }
}
