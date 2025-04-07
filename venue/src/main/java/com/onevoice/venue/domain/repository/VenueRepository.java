package com.onevoice.venue.domain.repository;

import java.util.Optional;

import com.onevoice.venue.domain.Venue;

public interface VenueRepository {

	Optional<Venue> findByName(String name);

	Venue save(Venue venue);
}
