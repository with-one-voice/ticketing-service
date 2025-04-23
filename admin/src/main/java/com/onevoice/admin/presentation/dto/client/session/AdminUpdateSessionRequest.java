package com.onevoice.admin.presentation.dto.client.session;

import java.time.LocalDate;
import java.time.LocalTime;

public record AdminUpdateSessionRequest(
        LocalDate sessionDate,
        LocalTime startTime,
        LocalTime endTime,
        Long seatPrice
) {}