package com.onevoice.ticket.domain;

import lombok.RequiredArgsConstructor;


public enum TicketStatus {

    WAITING_PAYMENT,
    CONFIRM_PAYMENT,
    CANCELLED,
    FAILED;

}
