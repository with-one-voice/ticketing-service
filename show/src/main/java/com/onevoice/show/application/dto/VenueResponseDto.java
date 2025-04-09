package com.onevoice.show.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record VenueResponseDto(
    UUID venueId,
    String name,
    String location,
    String description,
    Integer totalSeatCount,
    LocalDateTime createdAt,
    UUID createdBy
) {

}
