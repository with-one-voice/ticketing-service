package com.onevoice.venue.presentation.dto.response;

import java.util.UUID;

import com.onevoice.venue.application.dto.FindVenueQuery;

public record CreateVenueResponseDto(
	UUID venueId,
	String name,
	String location,
	String description,
	Integer totalSeatCount
) {
	public static CreateVenueResponseDto of(FindVenueQuery query) {
		return new CreateVenueResponseDto(
			query.venueId(),
			query.name(),
			query.location(),
			query.description(),
			query.totalSeatCount()
		);
	}
}