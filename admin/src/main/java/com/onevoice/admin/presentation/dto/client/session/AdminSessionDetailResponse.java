package com.onevoice.admin.presentation.dto.client.session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AdminSessionDetailResponse(
        UUID sessionId,
        UUID showId,
        String showTitle,
        LocalDate sessionDate,
        LocalTime startTime,
        LocalTime endTime,
        Integer seatCount,
        Integer seatPrice,
        String status
) {}