package com.onevoice.admin.presentation.dto.client;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminCreateShowRequest (
        UUID venueId,
        String title,
        String artist,
        String category,
        String posterUrl,
        String description,
        LocalDateTime ticketingStartTime,
        LocalDateTime ticketingEndTime
) {}