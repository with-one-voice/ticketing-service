package com.onevoice.payment.application.dto.client;

import com.onevoice.common.enumtype.TicketStatus;

public record UpdateTicketStatusRequestDto(
    TicketStatus status
) {
}
