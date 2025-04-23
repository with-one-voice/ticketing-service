package com.onevoice.admin.presentation.dto.client.show;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AdminUpdateShowRequest(
        String artist,
        String category,
        String posterUrl,
        String description,
        LocalDateTime ticketingStartTime,
        LocalDateTime ticketingEndTime
) {}