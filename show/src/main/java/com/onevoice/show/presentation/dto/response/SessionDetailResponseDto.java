package com.onevoice.show.presentation.dto.response;

import com.onevoice.show.domain.Session;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record SessionDetailResponseDto(
    UUID sessionId,
    UUID showId,
    String showName,
    LocalDate sessionDate,
    LocalTime startTime,
    LocalTime endTime,
    Integer seatCount,
    Long seatPrice
) {

    public static SessionDetailResponseDto of(Session session) {
        return new SessionDetailResponseDto(
            session.getId(),
            session.getShow().getId(),
            session.getShow().getTitle(),
            session.getSessionDate(),
            session.getStartTime(),
            session.getEndTime(),
            session.getSeatCount(),
            session.getSeatPrice()
        );
    }
}
