package com.onevoice.payment.application.dto.client;

public record UpdateTicketStatusRequestDto(
    TicketStatus status
) {

    public enum TicketStatus {

        WAITING_PAYMENT,
        CONFIRM_PAYMENT,
        CANCELLED,
        FAILED;
    }
}
