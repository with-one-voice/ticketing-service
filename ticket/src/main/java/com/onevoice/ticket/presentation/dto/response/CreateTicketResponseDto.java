package com.onevoice.ticket.presentation.dto.response;

import com.onevoice.ticket.domain.Ticket;

import com.onevoice.ticket.domain.TicketSeat;
import java.util.List;
import java.util.UUID;

public record CreateTicketResponseDto(
        UUID ticketId,
        UUID userId,
        String userName,
        UUID showId,
        String showName,
        List<UUID> seatId
) {
    public static CreateTicketResponseDto of(Ticket ticket) {
        return new CreateTicketResponseDto(ticket.getId(), ticket.getUserId(), ticket.getUserName(),
            ticket.getSessionId(), ticket.getShowName(), ticket.getTicketSeatList().stream().map(
            TicketSeat::getSeatId).toList());
    }
}
