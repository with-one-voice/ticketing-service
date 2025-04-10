package com.onevoice.ticket.application.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record SessionDetailsQuery(
    UUID sessionId,
    UUID showId,
    String showName,
    LocalDate sessionDate,
    LocalTime startTime,
    LocalTime endTime,
    Integer seatCount,
    Long seatPrice
) {

}
