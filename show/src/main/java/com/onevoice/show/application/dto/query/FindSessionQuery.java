package com.onevoice.show.application.dto.query;

import com.onevoice.show.domain.Session;
import com.onevoice.show.domain.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record FindSessionQuery(
    UUID sessionId,
    UUID showId,
    LocalDate sessionDate,
    LocalTime startTime,
    LocalTime endTime,
    Integer seatCount,
    Long seatPrice,
    Status status,
    LocalDateTime createdAt,
    UUID createdBy,
    LocalDateTime updatedAt,
    UUID updatedBy,
    LocalDateTime deletedAt,
    UUID deletedBy
) {

    public static FindSessionQuery of(Session session) {
        return new FindSessionQuery(
            session.getId(),
            session.getShow().getId(),
            session.getSessionDate(),
            session.getStartTime(),
            session.getEndTime(),
            session.getSeatCount(),
            session.getSeatPrice(),
            session.getStatus(),
            session.getCreatedAt(),
            session.getCreatedBy(),
            session.getUpdatedAt(),
            session.getUpdatedBy(),
            session.getDeletedAt(),
            session.getDeletedBy()
        );
    }
}
