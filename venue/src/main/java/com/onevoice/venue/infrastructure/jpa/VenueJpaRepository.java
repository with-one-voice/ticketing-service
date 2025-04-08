package com.onevoice.venue.infrastructure.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onevoice.venue.domain.Venue;

public interface VenueJpaRepository extends JpaRepository<Venue, UUID> {

}
