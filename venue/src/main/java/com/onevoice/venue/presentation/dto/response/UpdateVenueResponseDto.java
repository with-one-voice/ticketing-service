package com.onevoice.venue.presentation.dto.response;

import com.onevoice.venue.application.dto.FindVenueQuery;
import java.util.UUID;

public record UpdateVenueResponseDto(
    UUID venueId,
    String name,
    String location,
    String description,
    Integer totalSeatCount
) {

    public static UpdateVenueResponseDto of(FindVenueQuery query) {
        return new UpdateVenueResponseDto(
            query.venueId(),
            query.name(),
            query.location(),
            query.description(),
            query.totalSeatCount()
        );
    }
}
