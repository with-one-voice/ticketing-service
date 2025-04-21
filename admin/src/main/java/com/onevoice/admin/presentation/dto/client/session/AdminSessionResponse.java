package com.onevoice.admin.presentation.dto.client.session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AdminSessionResponse(
        UUID sessionId,
        UUID showId,
        LocalDate sessionDate,
        LocalTime startTime,
        LocalTime endTime,
        int seatCount,
        int seatPrice,
        String status
) {}