package com.onevoice.show.application.dto;

import java.util.UUID;

public record CreateSeatRequestDto(
    UUID sessionId,
    int seatCount,
    int price
) {

}
