package com.onevoice.payment.application.dto.message;

import com.onevoice.common.enumtype.TicketStatus;
import java.util.UUID;

public record TicketMessage(
    UUID ticketId,
    UUID userId,
    TicketStatus ticketStatus
) {
}
