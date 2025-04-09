package com.onevoice.show.application.dto;

import java.util.UUID;

public record SeatResponseDto(
    UUID seatId,
    UUID sessionId,
    String seatCode,
    String status,
    int price
) {

}
