package com.onevoice.show.presentation.dto.request;

import com.onevoice.show.domain.ShowCategory;
import java.time.LocalDateTime;

public record UpdateShowRequestDto(
    String artist,
    ShowCategory category,
    String posterUrl,
    String description,
    LocalDateTime ticketingStartTime,
    LocalDateTime ticketingEndTime
) {

}
