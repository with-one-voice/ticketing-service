package com.onevoice.ticket.presentation.dto.response;

import com.onevoice.ticket.domain.Ticket;

import java.util.UUID;

public record DeleteTicketResponseDto(
        UUID ticketId,
        String userName,
        String showName
) {
    public static DeleteTicketResponseDto of(Ticket ticket){
        return new DeleteTicketResponseDto(ticket.getId(),ticket.getUserName(),ticket.getShowName());
    }
}
