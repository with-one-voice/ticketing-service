package com.onevoice.admin.presentation.dto.client.show;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminUpdateShowResponse(
        UUID showId,
        UUID venueId,
        String title,
        String artist,
        String category,
        String posterUrl,
        String description,
        LocalDateTime ticketingStartTime,
        LocalDateTime ticketingEndTime,
        String status
) {}