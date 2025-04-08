package com.onevoice.venue.presentation.dto.request;

public record CreateVenueRequestDto(
	String name,
	String location,
	String description,
	Integer totalSeatCount
) {

}
