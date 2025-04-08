package com.onevoice.seat.presentation.dto.response;

import com.onevoice.seat.domain.Seat;

import java.util.UUID;

public record SeatResponseDto(UUID seatId,
                              String seatCode,
                              String status,
                              int price) {
    public static SeatResponseDto of(Seat seat) {
        return new SeatResponseDto(
                seat.getSeatId(),
                seat.getSeatCode().getValue(),
                seat.getStatus().name(),
                seat.getPrice().getValue()
        );
    }
}
