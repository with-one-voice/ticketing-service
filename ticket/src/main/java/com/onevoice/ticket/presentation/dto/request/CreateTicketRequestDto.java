package com.onevoice.ticket.presentation.dto.request;

import java.util.List;
import java.util.UUID;

public record CreateTicketRequestDto(
        UUID userId,
        UUID sessionId,
        List<UUID> seatIds
) {
}
