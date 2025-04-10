package com.onevoice.ticket.application.service;

import com.onevoice.ticket.application.client.SeatClient;
import com.onevoice.ticket.application.client.ShowClient;
import com.onevoice.ticket.application.client.UserClient;
import com.onevoice.ticket.application.dto.FindHoldSeatQuery;
import com.onevoice.ticket.application.dto.FindUserQuery;
import com.onevoice.ticket.application.dto.HoldSeatCommand;
import com.onevoice.ticket.application.dto.SessionDetailsQuery;
import com.onevoice.ticket.domain.Ticket;
import com.onevoice.ticket.domain.TicketStatus;
import com.onevoice.ticket.domain.repository.TicketRepository;
import com.onevoice.ticket.exception.RemoteUserNotFoundException;
import com.onevoice.ticket.exception.TicketNotFoundException;
import com.onevoice.ticket.exception.TicketOwnerMismatchException;
import com.onevoice.ticket.exception.TicketSessionNotFoundException;
import com.onevoice.ticket.presentation.dto.request.CreateTicketRequestDto;
import com.onevoice.ticket.presentation.dto.request.UpdateTicketStatusRequestDto;
import com.onevoice.ticket.presentation.dto.response.CreateTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.DeleteTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.FindTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.ListReservedTicketResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService{

    private final TicketRepository ticketRepository;
    private final UserClient userClient;
    private final ShowClient showClient;
    private final SeatClient seatClient;

    @Override
    public CreateTicketResponseDto createTicket(CreateTicketRequestDto requestDto) {

        FindUserQuery userQuery = userClient.findUserById(requestDto.userId()).orElseThrow(RemoteUserNotFoundException::new);
        String userName = userQuery.email();

        SessionDetailsQuery sessionquery = showClient.getSessionDetail(requestDto.sessionId())
            .orElseThrow(TicketSessionNotFoundException::new);
        String showName = sessionquery.showName();

        //TODO 좌석 선점 가능 여부 확인

        List<UUID> seatIdList = new ArrayList<>();
        seatIdList.add(requestDto.seatId());
        HoldSeatCommand seatIds = new HoldSeatCommand(seatIdList);

        FindHoldSeatQuery findHoldSeatQuery = seatClient.holdSeatsInternal(requestDto.sessionId(),
            seatIds).orElseThrow();

        log.info("FindHoldSeatQuery seatCode: {}", findHoldSeatQuery.seatIds().get(0));

        Ticket ticket = new Ticket(requestDto.userId(),userName, requestDto.sessionId(),showName,requestDto.seatId());

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

        if (!ticket.getUserId().equals(userId)) {

            throw new TicketOwnerMismatchException();
        }
        return FindTicketResponseDto.of(ticket);
    }

    @Override
    @Transactional
    public FindTicketResponseDto updateTicketStatus(UUID ticketId, UUID userId, UpdateTicketStatusRequestDto requestDto) {

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);

        if (!ticket.getUserId().equals(userId)) {

            throw new TicketOwnerMismatchException();
        }

        TicketStatus newStatus = requestDto.status();

        ticket.updateTicketStatus(newStatus);

        return FindTicketResponseDto.of(ticket);
    }

    @Override
    @Transactional
    public FindTicketResponseDto updateTicketStatus(UUID ticketID,
        UpdateTicketStatusRequestDto requestDto) {

        Ticket ticket = ticketRepository.findById(ticketID).orElseThrow(TicketNotFoundException::new);

        TicketStatus newStatus = requestDto.status();
        if (newStatus == TicketStatus.CONFIRM_PAYMENT) {
            // 결제 확정

        }else{
            // 결제 실패

        }

        return FindTicketResponseDto.of(ticket);
    }

    @Override
    @Transactional
    public FindTicketResponseDto expireTicket(UUID ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);

        TicketStatus newStatus = TicketStatus.EXPIRED;
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
