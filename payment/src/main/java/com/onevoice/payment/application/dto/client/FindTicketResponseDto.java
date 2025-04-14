package com.onevoice.payment.application.dto.client;

import com.onevoice.common.enumtype.TicketStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record FindTicketResponseDto(
    UUID ticketId,
    UUID userId,
    String userName,
    UUID showId,
    String showName,
    UUID seatId,
    TicketStatus status,
    LocalDateTime reservedAt
) {
}
