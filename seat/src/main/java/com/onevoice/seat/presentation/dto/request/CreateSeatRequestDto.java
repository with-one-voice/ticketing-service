package com.onevoice.seat.presentation.dto.request;

import java.util.UUID;

public record CreateSeatRequestDto(
        UUID sessionId,
        int seatCount,
        int price
) {
}
