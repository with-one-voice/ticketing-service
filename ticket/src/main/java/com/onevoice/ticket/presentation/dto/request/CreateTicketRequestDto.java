package com.onevoice.ticket.presentation.dto.request;

import java.util.UUID;

public record CreateTicketRequestDto(
        UUID userId,
        UUID sessionId,
        UUID seatId
) {
}
