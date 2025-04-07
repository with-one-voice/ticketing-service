package com.onevoice.venue.domain.repository;

import com.onevoice.venue.domain.Venue;
import java.util.Optional;
import java.util.UUID;

public interface VenueRepository {

    Optional<Venue> findByName(String name);

    Optional<Venue> findById(UUID venueId);

    Venue save(Venue venue);

}
