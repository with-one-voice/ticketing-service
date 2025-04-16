package com.onevoice.ticket.application.dto;

import java.util.UUID;

public record FindSeatQuery(
    UUID seatId,
    UUID sessionId,
    String seatCode,
    String status,
    int price
) {

}
