package com.onevoice.seat.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record HoldSeatResponseDto(
        String message,
        LocalDateTime expireAt,
        List<UUID>seatIds
) {
    public static HoldSeatResponseDto success(LocalDateTime expireAt,List<UUID> seatIds) {
        return new HoldSeatResponseDto("선점 성공", expireAt, seatIds);
    }
}

