package com.onevoice.payment.application.dto.message;

import java.util.UUID;

public record TicketMessage(
    UUID ticketId,
    TicketStatus ticketStatus
) {

    public enum TicketStatus {

        WAITING_PAYMENT,
        CONFIRM_PAYMENT,
        CANCELLED,
        EXPIRED;
    }
}
