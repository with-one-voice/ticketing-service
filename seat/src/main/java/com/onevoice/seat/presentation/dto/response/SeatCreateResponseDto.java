package com.onevoice.seat.presentation.dto.response;

import com.onevoice.seat.domain.Seat;

import java.util.UUID;

public record SeatCreateResponseDto(UUID seatId,
                                    UUID sessionId,
                                    String seatCode,
                                    String status,
                                    int price
) {
    public static SeatCreateResponseDto of(Seat seat) {
        return new SeatCreateResponseDto(
                seat.getSeatId(),
                seat.getSessionId().getValue(),
                seat.getSeatCode().getValue(),
                seat.getStatus().name(),
                seat.getPrice().getValue()
        );
    }

}
