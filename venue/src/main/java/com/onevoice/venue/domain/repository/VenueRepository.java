package com.onevoice.venue.domain.repository;

import com.onevoice.venue.domain.Venue;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface VenueRepository {

    Optional<Venue> findByName(String name);

    Optional<Venue> findById(UUID venueId);

    List<Venue> findAll();

    Venue save(Venue venue);

    List<Venue> search(String keyword, Pageable pageable);

    long getTotal(String keyword);
}
