package com.onevoice.ticket.application.service;

import com.onevoice.ticket.application.client.UserClient;
import com.onevoice.ticket.application.dto.FindUserQuery;
import com.onevoice.ticket.domain.Ticket;
import com.onevoice.ticket.domain.TicketStatus;
import com.onevoice.ticket.domain.repository.TicketRepository;
import com.onevoice.ticket.exception.RemoteUserNotFoundException;
import com.onevoice.ticket.exception.TicketNotFoundException;
import com.onevoice.ticket.exception.TicketOwnerMismatchException;
import com.onevoice.ticket.presentation.dto.request.CreateTicketRequestDto;
import com.onevoice.ticket.presentation.dto.request.UpdateTicketStatusRequestDto;
import com.onevoice.ticket.presentation.dto.response.CreateTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.DeleteTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.FindTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.ListReservedTicketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService{

    private final TicketRepository ticketRepository;
    private final UserClient userClient;

    @Override
    public CreateTicketResponseDto createTicket(CreateTicketRequestDto requestDto) {
        /**
         * TODO user,show,seat 확인 로직
         */
        FindUserQuery userQuery = userClient.findUserById(requestDto.userId()).orElseThrow(RemoteUserNotFoundException::new);
        String userName = userQuery.email();
        String showName = "dummy show";

        Ticket ticket = new Ticket(requestDto.userId(),userName, requestDto.showId(),showName,requestDto.seatId());

        Ticket saved = ticketRepository.save(ticket);

        return CreateTicketResponseDto.of(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ListReservedTicketResponseDto> getReservedTickets(UUID userID, int page, int size, String sortBy, boolean isAsc) {

        Sort.Direction direction =isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Ticket> ticketList = ticketRepository.searchByUserId(userID, pageable);

        return ticketList.map(ListReservedTicketResponseDto::of);
    }

    @Override
    @Transactional(readOnly = true)
    public FindTicketResponseDto getTicket(UUID ticketId, UUID userId) {

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);

        if(ticket.getUserId() != userId){
            throw new TicketOwnerMismatchException();
        }
        return FindTicketResponseDto.of(ticket);
    }

    @Override
    @Transactional
    public FindTicketResponseDto updateTicketStatus(UUID ticketId, UUID userId, UpdateTicketStatusRequestDto requestDto) {

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);

        if(ticket.getUserId() != userId){
            throw new TicketOwnerMismatchException();
        }

        TicketStatus newStatus = requestDto.status();
        ticket.updateTicketStatus(newStatus);

        return FindTicketResponseDto.of(ticket);
    }

    @Override
    @Transactional
    public FindTicketResponseDto expireTicket(UUID ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);

        TicketStatus newStatus = TicketStatus.FAILED;
        ticket.updateTicketStatus(newStatus);
        return FindTicketResponseDto.of(ticket);
    }

    @Override
    @Transactional
    public DeleteTicketResponseDto deleteTicket(UUID ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);

        TicketStatus newStatus = TicketStatus.CANCELLED;
        ticket.updateTicketStatus(newStatus);

        return DeleteTicketResponseDto.of(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ListReservedTicketResponseDto> searchTickets(UUID userId, int page, int size, String sortBy, boolean isAsc, String keyword) {

        Sort.Direction direction =isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Ticket> ticketList = ticketRepository.searchTicketByKeyword(userId, pageable,keyword);

        return ticketList.map(ListReservedTicketResponseDto::of);
    }
}
