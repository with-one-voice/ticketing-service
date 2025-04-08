package com.onevoice.show.infrastructure;

import com.onevoice.show.domain.QShow;
import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.infrastructure.jpa.ShowJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShowRepositoryImpl implements ShowRepository {

    private final ShowJpaRepository showJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Show> findByTitle(String title) {
        QShow show = QShow.show;
        return Optional.ofNullable(queryFactory
            .selectFrom(show)
            .where(show.title.eq(title))
            .where(show.deletedAt.isNull())
            .fetchFirst());
    }

    @Override
    public Show save(Show show) {
        return showJpaRepository.save(show);
    }

    @Override
    public Optional<Show> findById(UUID showId) {
        QShow show = QShow.show;
        return Optional.ofNullable(queryFactory
            .selectFrom(show)
            .where(show.id.eq(showId))
            .where(show.deletedAt.isNull())
            .fetchFirst());
    }

    @Override
    public List<Show> findAll() {
        QShow show = QShow.show;
        return queryFactory
            .selectFrom(show)
            .where(show.deletedAt.isNull())
            .fetch();
    }
}
