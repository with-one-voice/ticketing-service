package com.onevoice.seat.presentation.dto.request;

import com.onevoice.seat.domain.SeatStatus;

import java.util.List;
import java.util.UUID;

public record SeatStatusChangeRequestDto(
        List<UUID> seatIds,
        SeatStatus newStatus
) {
}
