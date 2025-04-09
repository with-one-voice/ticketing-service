package com.onevoice.show.presentation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateSessionRequestDto(
    LocalDate sessionDate,
    LocalTime startTime,
    LocalTime endTime,
    Integer seatCount,
    Long seatPrice
) {

}
