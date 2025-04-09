package com.onevoice.show.presentation.dto.response;

import com.onevoice.show.application.dto.FindShowQuery;
import com.onevoice.show.domain.ShowCategory;
import com.onevoice.show.domain.Status;
import java.time.LocalDateTime;
import java.util.UUID;

public record ShowResponseDto(
    UUID showId,
    UUID venueId,
    String title,
    String artist,
    ShowCategory category,
    String posterUrl,
    String description,
    LocalDateTime ticketingStartTime,
    LocalDateTime ticketingEndTime,
    Status status,
    LocalDateTime createdAt,
    UUID createdBy
) {

    public static ShowResponseDto of(FindShowQuery query) {
        return new ShowResponseDto(
            query.showId(),
            query.venueId(),
            query.title(),
            query.artist(),
            query.category(),
            query.posterUrl(),
            query.description(),
            query.ticketingStartTime(),
            query.ticketingEndTime(),
            query.status(),
            query.createdAt(),
            query.createdBy()
        );
    }
}
