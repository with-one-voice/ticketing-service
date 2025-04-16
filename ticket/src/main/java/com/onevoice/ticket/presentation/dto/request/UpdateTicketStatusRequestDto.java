package com.onevoice.ticket.presentation.dto.request;

import com.onevoice.common.enumtype.TicketStatus;

public record UpdateTicketStatusRequestDto(
        TicketStatus status
) {
}
