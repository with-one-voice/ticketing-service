package com.onevoice.admin.presentation.dto.client.session;

import java.time.LocalDate;
import java.time.LocalTime;

public record AdminCreateSessionRequest (
        LocalDate sessionDate,
        LocalTime startTime,
        LocalTime endTime,
        Integer seatCount,
        Long seatPrice
) {}