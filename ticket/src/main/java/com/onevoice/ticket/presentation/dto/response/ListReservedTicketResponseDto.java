package com.onevoice.ticket.presentation.dto.response;

import com.onevoice.ticket.domain.Ticket;

import java.util.List;
import java.util.UUID;

public record ListReservedTicketResponseDto(
        UUID ticketId,
        String userName,
        String showName
) {
    public static ListReservedTicketResponseDto of(Ticket ticket) {
        return new ListReservedTicketResponseDto(ticket.getId(), ticket.getUserName(), ticket.getShowName());
    }

}
