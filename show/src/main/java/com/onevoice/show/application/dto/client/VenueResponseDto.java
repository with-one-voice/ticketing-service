package com.onevoice.show.application.dto.client;

import java.util.UUID;

public record VenueResponseDto(
    UUID venueId,
    String name,
    String location,
    String description,
    Integer totalSeatCount
) {

}
