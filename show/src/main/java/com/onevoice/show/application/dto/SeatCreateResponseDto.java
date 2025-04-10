package com.onevoice.show.application.dto;

import java.util.UUID;

public record SeatCreateResponseDto(
    UUID sessionId,
    int seatCount,
    int price
) {

}
