package com.onevoice.show.presentation.dto.request;

import com.onevoice.show.domain.ShowCategory;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateShowRequestDto(
    UUID venueId,
    String title,
    String artist,
    ShowCategory category,
    String posterUrl,
    String description,
    LocalDateTime ticketingStartTime,
    LocalDateTime ticketingEndTime
) {

}
