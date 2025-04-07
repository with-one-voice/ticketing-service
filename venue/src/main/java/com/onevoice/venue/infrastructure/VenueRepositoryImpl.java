package com.onevoice.venue.infrastructure;

import com.onevoice.venue.domain.QVenue;
import com.onevoice.venue.domain.Venue;
import com.onevoice.venue.domain.repository.VenueRepository;
import com.onevoice.venue.infrastructure.jpa.VenueJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
            .fetchFirst()
        );
    }

    @Override
    public Venue save(Venue venue) {
        return jpaRepository.save(venue);
    }

}
