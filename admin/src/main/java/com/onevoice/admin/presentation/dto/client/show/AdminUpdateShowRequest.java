package com.onevoice.admin.presentation.dto.client.show;

import java.time.LocalDateTime;

public record AdminUpdateShowRequest(
        String title,
        String artist,
        String category,
        String posterUrl,
        String description,
        LocalDateTime ticketingStartTime,
        LocalDateTime ticketingEndTime,
        String status
) {}