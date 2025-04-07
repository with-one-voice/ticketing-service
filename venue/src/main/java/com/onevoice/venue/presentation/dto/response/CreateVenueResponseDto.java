package com.onevoice.venue.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.onevoice.venue.domain.Venue;
import com.onevoice.venue.presentation.dto.request.CreateVenueRequestDto;

public record CreateVenueResponseDto(
	UUID venueId,
	String name,
	String location,
	String description,
	Integer totalSeatCount
) {
	public static CreateVenueResponseDto of(Venue venue) {
		return new CreateVenueResponseDto(
			venue.getId(),
			venue.getName(),
			venue.getLocation(),
			venue.getDescription(),
			venue.getTotalSeatCount()
		);
	}
}