package com.onevoice.show.presentation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateSessionRequestDto(
    LocalDate sessionDate,
    LocalTime startTime,
    LocalTime endTime,
    Long seatPrice
) {

}
