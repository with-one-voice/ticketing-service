package com.onevoice.show.presentation.dto.response;

import com.onevoice.show.application.dto.FindSessionQuery;
import com.onevoice.show.domain.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record SessionResponseDto(
    UUID sessionId,
    UUID showId,
    LocalDate sessionDate,
    LocalTime startTime,
    LocalTime endTime,
    Integer seatCount,
    Long seatPrice,
    Status status,
    LocalDateTime createdAt,
    UUID createdBy
) {

    public static SessionResponseDto of(FindSessionQuery query) {
        return new SessionResponseDto(
            query.sessionId(),
            query.showId(),
            query.sessionDate(),
            query.startTime(),
            query.endTime(),
            query.seatCount(),
            query.seatPrice(),
            query.status(),
            query.createdAt(),
            query.createdBy()
        );
    }
}
