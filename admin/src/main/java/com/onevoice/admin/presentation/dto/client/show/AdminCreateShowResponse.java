package com.onevoice.admin.presentation.dto.client.show;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminCreateShowResponse(
        UUID showId,
        String title,
        String artist,
        String category,
        LocalDateTime ticketingStartTime,
        LocalDateTime ticketingEndTime,
        String status
) {}