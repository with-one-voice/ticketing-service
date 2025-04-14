package com.onevoice.venue.infrastructure;

import com.onevoice.venue.domain.QVenue;
import com.onevoice.venue.domain.Venue;
import com.onevoice.venue.domain.repository.VenueRepository;
import com.onevoice.venue.infrastructure.jpa.VenueJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VenueRepositoryImpl implements VenueRepository {

    private final VenueJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Venue> findByName(String name) {
        QVenue venue = QVenue.venue;
        return Optional.ofNullable(queryFactory
            .selectFrom(venue)
            .where(venue.name.eq(name))
            .fetchFirst()
        );
    }

    @Override
    public Optional<Venue> findById(UUID venueId) {
        QVenue venue = QVenue.venue;
        return Optional.ofNullable(queryFactory
            .selectFrom(venue)
            .where(venue.id.eq(venueId))
            .where(venue.deletedAt.isNull())
            .fetchFirst()
        );
    }

    @Override
    public List<Venue> findAll() {
        QVenue venue = QVenue.venue;
        return queryFactory
            .selectFrom(venue)
            .where(venue.deletedAt.isNull())
            .fetch();
    }

    @Override
    public Venue save(Venue venue) {
        return jpaRepository.save(venue);
    }

    @Override
    public List<Venue> search(String keyword, Pageable pageable) {
        QVenue venue = QVenue.venue;

        return queryFactory
            .selectFrom(venue)
            .where(
                venue.name.containsIgnoreCase(keyword)
                    .or(venue.location.containsIgnoreCase(keyword)
                        .or(venue.description.containsIgnoreCase(keyword)))
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public long getTotal(String keyword) {
        QVenue venue = QVenue.venue;

        Long result = queryFactory
            .select(venue.count())
            .from(venue)
            .where(
                venue.name.containsIgnoreCase(keyword)
                    .or(venue.location.containsIgnoreCase(keyword))
            )
            .fetchOne();

        return result != null ? result : 0L;
    }

}
