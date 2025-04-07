package com.onevoice.venue.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.onevoice.venue.domain.QVenue;
import com.onevoice.venue.domain.Venue;
import com.onevoice.venue.domain.repository.VenueRepository;
import com.onevoice.venue.infrastructure.jpa.VenueJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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
			.fetchFirst());
	}

	@Override
	public Venue save(Venue venue) {
		return jpaRepository.save(venue);
	}
}
