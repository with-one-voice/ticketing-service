package com.onevoice.show.application.dto.query;

import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.ShowCategory;
import com.onevoice.show.domain.Status;
import java.time.LocalDateTime;
import java.util.UUID;

public record FindShowQuery(
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
    UUID createdBy,
    LocalDateTime updatedAt,
    UUID updatedBy,
    LocalDateTime deletedAt,
    UUID deletedBy
) {

    public static FindShowQuery of(Show save) {
        return new FindShowQuery(
            save.getId(),
            save.getVenueId(),
            save.getTitle(),
            save.getArtist(),
            save.getCategory(),
            save.getPosterUrl(),
            save.getDescription(),
            save.getTicketingStartTime(),
            save.getTicketingEndTime(),
            save.getStatus(),
            save.getCreatedAt(),
            save.getCreatedBy(),
            save.getUpdatedAt(),
            save.getUpdatedBy(),
            save.getDeletedAt(),
            save.getDeletedBy()
        );
    }
}
