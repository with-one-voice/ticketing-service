package com.onevoice.show.application.dto.client;

import java.util.UUID;

public record SeatCreateRequestDto(
    UUID sessionId,
    int seatCount,
    int price
) {

}
