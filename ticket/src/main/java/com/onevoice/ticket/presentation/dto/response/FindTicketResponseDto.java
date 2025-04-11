package com.onevoice.ticket.presentation.dto.response;

import com.onevoice.ticket.domain.Ticket;
import com.onevoice.ticket.domain.TicketStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record FindTicketResponseDto(
        UUID ticketId,
        UUID userId,
        String userName,
        UUID showId,
        String showName,
        UUID seatId,
        TicketStatus status,
        LocalDateTime reservedAt
) {
    public static FindTicketResponseDto of(Ticket ticket) {
        return new FindTicketResponseDto(ticket.getId(), ticket.getUserId(), ticket.getUserName() ,ticket.getSessionId(),ticket.getShowName() ,ticket.getSeatId(), ticket.getStatus(), ticket.getReservedAt());
    }
}
