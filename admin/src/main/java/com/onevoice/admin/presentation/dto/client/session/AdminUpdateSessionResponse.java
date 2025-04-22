package com.onevoice.admin.presentation.dto.client.session;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AdminUpdateSessionResponse (
        UUID sessionId,
        UUID showId,
        LocalDate sessionDate,
        LocalTime startTime,
        LocalTime endTime,
        Integer seatCount,
        Long seatPrice,
        String status
) {}