package com.onevoice.ticket.presentation.dto.request;

import com.onevoice.ticket.domain.TicketStatus;

public record UpdateTicketStatusRequestDto(
        TicketStatus status
) {
}
