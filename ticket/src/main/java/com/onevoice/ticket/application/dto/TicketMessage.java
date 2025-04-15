package com.onevoice.ticket.application.dto;

import com.onevoice.common.enumtype.TicketStatus;
import java.util.UUID;

public record TicketMessage(
    UUID ticketId,
    TicketStatus ticketStatus
) {

}
