package com.onevoice.ticket.presentation.dto.response;

import com.onevoice.common.enumtype.TicketStatus;
import com.onevoice.ticket.domain.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record FindTicketResponseDto(
        UUID ticketId,
        UUID userId,
        String userName,
        UUID showId,
        String showName,
        List<UUID> seatId,
        TicketStatus status,
        LocalDateTime reservedAt
) {
    public static FindTicketResponseDto of(Ticket ticket) {
        return new FindTicketResponseDto(ticket.getId(), ticket.getUserId(), ticket.getUserName() ,ticket.getSessionId(),ticket.getShowName() ,ticket.getSeatIdList(), ticket.getStatus(), ticket.getReservedAt());
    }
}
