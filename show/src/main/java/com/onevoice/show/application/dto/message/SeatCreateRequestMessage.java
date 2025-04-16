package com.onevoice.show.application.dto.message;

import java.util.UUID;

public record SeatCreateRequestMessage(
    UUID sessionId,
    int seatCount,
    int price
) {

}
