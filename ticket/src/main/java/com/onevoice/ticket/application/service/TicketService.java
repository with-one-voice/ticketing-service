package com.onevoice.ticket.application.service;

import com.onevoice.ticket.presentation.dto.request.CreateTicketRequestDto;
import com.onevoice.ticket.presentation.dto.request.UpdateTicketStatusRequestDto;
import com.onevoice.ticket.presentation.dto.response.CreateTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.DeleteTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.FindTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.ListReservedTicketResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface TicketService {
    CreateTicketResponseDto createTicket(CreateTicketRequestDto requestDto);

    Page<ListReservedTicketResponseDto> getReservedTickets(UUID userID, int page, int size, String sortBy, boolean isAsc);

    FindTicketResponseDto getTicket(UUID ticketId, UUID userId);

    FindTicketResponseDto updateTicketStatus(UUID ticketId, UUID userId, UpdateTicketStatusRequestDto requestDto);

    FindTicketResponseDto updateTicketStatus(UUID ticketID, UpdateTicketStatusRequestDto requestDto);

    FindTicketResponseDto expireTicket(UUID ticketId);

    DeleteTicketResponseDto deleteTicket(UUID ticketId);

    Page<ListReservedTicketResponseDto> searchTickets(UUID userId, int page, int size, String sortBy, boolean isAsc, String keyword);

    void confirmTicketAfterPayment(UUID ticketId);
    void failTicketAfterPayment(UUID ticketId);
}
