package com.onevoice.venue.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onevoice.venue.domain.Venue;

public record FindVenueQuery(
	UUID venueId,
	String name,
	String location,
	String description,
	Integer totalSeatCount,
	LocalDateTime createAt,
	UUID createdBy,
	LocalDateTime updateAt,
	UUID updatedBy,
	LocalDateTime deleteAt,
	UUID deletedBy
) {

	public static FindVenueQuery of(Venue venue){
		return new FindVenueQuery(
			venue.getId(),
			venue.getName(),
			venue.getLocation(),
			venue.getDescription(),
			venue.getTotalSeatCount(),
			venue.getCreatedAt(),
			venue.getCreatedBy(),
			venue.getUpdatedAt(),
			venue.getUpdatedBy(),
			venue.getDeletedAt(),
			venue.getDeletedBy()
		);
	}
}
