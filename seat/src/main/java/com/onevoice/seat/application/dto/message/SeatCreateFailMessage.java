package com.onevoice.seat.application.dto.message;

import java.util.UUID;

public record SeatCreateFailMessage(
    UUID sessionId
) {

}
