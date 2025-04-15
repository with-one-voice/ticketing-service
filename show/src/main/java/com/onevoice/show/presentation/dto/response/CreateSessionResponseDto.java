package com.onevoice.show.presentation.dto.response;

import com.onevoice.show.application.dto.query.FindSessionQuery;
import com.onevoice.show.domain.Status;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateSessionResponseDto(
    UUID sessionId,
    UUID showId,
    LocalDate sessionDate,
    LocalTime startTime,
    LocalTime endTime,
    Integer seatCount,
    Long seatPrice,
    Status status
) {

    public static CreateSessionResponseDto of(FindSessionQuery query) {
        return new CreateSessionResponseDto(
            query.sessionId(),
            query.showId(),
            query.sessionDate(),
            query.startTime(),
            query.endTime(),
            query.seatCount(),
            query.seatPrice(),
            query.status()
        );
    }
}
