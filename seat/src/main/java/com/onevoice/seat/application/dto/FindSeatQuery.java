package com.onevoice.seat.application.dto;

import com.onevoice.seat.domain.Seat;

import java.util.UUID;

public record FindSeatQuery(UUID seatId, String seatCode, UUID sessionId, String status, int price) {
    public static FindSeatQuery of(Seat seat) {
        return new FindSeatQuery(
                seat.getSeatId(),
                seat.getSeatCode().getValue(),
                seat.getSessionId().getValue(),
                seat.getStatus().name(),
                seat.getPrice().getValue()
        );
    }
}