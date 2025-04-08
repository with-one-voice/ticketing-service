package com.onevoice.venue.presentation.dto.response;

import com.onevoice.venue.application.dto.FindVenueQuery;
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

    public static VenueResponseDto of(FindVenueQuery query) {
        return new VenueResponseDto(
            query.venueId(),
            query.name(),
            query.location(),
            query.description(),
            query.totalSeatCount(),
            query.createAt(),
            query.createdBy()
        );
    }
}
