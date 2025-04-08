package com.onevoice.venue.presentation.dto.request;

public record UpdateVenueRequestDto(
    String description,
    Integer totalSeatCount
) {

}
