package com.onevoice.seat.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record HoldSeatRequestDto(
        @NotEmpty(message = "좌석 코드는 하나 이상 선택되어야 합니다.")
        List<String> seatCodes
) {
}
