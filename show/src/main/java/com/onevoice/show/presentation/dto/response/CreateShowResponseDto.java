package com.onevoice.show.presentation.dto.response;

import com.onevoice.show.application.dto.query.FindShowQuery;
import com.onevoice.show.domain.ShowCategory;
import com.onevoice.show.domain.Status;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateShowResponseDto(
    UUID showId,
    UUID venueId,
    String title,
    String artist,
    ShowCategory category,
    String posterUrl,
    String description,
    LocalDateTime ticketingStartTime,
    LocalDateTime ticketingEndTime,
    Status status
) {

    public static CreateShowResponseDto of(FindShowQuery query) {
        return new CreateShowResponseDto(
            query.showId(),
            query.venueId(),
            query.title(),
            query.artist(),
            query.category(),
            query.posterUrl(),
            query.description(),
            query.ticketingStartTime(),
            query.ticketingEndTime(),
            query.status()
        );
    }
}
