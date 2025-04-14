package com.onevoice.ticket.application.service;

import com.onevoice.common.enumtype.SeatStatus;
import com.onevoice.common.enumtype.TicketStatus;
import com.onevoice.ticket.application.client.SeatClient;
import com.onevoice.ticket.application.client.ShowClient;
import com.onevoice.ticket.application.client.UserClient;
import com.onevoice.ticket.application.dto.FindUserQuery;
import com.onevoice.ticket.application.dto.HoldSeatCommand;
import com.onevoice.ticket.application.dto.SessionDetailsQuery;
import com.onevoice.ticket.application.dto.UpdateSeatStatusCommand;
import com.onevoice.ticket.domain.Ticket;
import com.onevoice.ticket.domain.repository.TicketRepository;
import com.onevoice.ticket.exception.DuplicateTicketException;
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
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
    private final RedissonClient redissonClient;

    @Override
    public CreateTicketResponseDto createTicket(CreateTicketRequestDto requestDto) {

        try {

            // 1. 사용자 조회
            FindUserQuery userQuery = userClient.findUserById(requestDto.userId())
                .orElseThrow(RemoteUserNotFoundException::new);
            String userName = userQuery.email();

            // 2. 세션 정보 조회
            SessionDetailsQuery sessionQuery = showClient.getSessionDetail(requestDto.sessionId())
                .orElseThrow(TicketSessionNotFoundException::new);
            String showName = sessionQuery.showName();

            // 3. 좌석 선점 요청
            List<UUID> seatIdList = List.of(requestDto.seatId());
            HoldSeatCommand holdCommand = new HoldSeatCommand(seatIdList);

            seatClient.holdSeatsInternal(requestDto.sessionId(), holdCommand)
                .orElseThrow(() -> new IllegalStateException("좌석 선점 실패"));

            // 4. 티켓 저장
            Ticket ticket = new Ticket(
                requestDto.userId(),
                userName,
                requestDto.sessionId(),
                showName,
                requestDto.seatId()
            );
            Ticket saved = ticketRepository.save(ticket);

            return CreateTicketResponseDto.of(saved);

        }catch (DataIntegrityViolationException e){
            if (e.getMessage().contains("uk_session_seat")) {
                throw new DuplicateTicketException();
            }
            throw e;
        }
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
        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);

            if (!ticket.getUserId().equals(userId)) {
                throw new TicketOwnerMismatchException();
            }

            TicketStatus newStatus = requestDto.status();
            ticket.updateTicketStatus(newStatus);

            return FindTicketResponseDto.of(ticket);

        } catch (ObjectOptimisticLockingFailureException e) {
            // 다른 트랜잭션이 먼저 상태를 변경한 경우 발생
            throw new IllegalStateException("다른 요청에서 이미 상태가 변경되었습니다. 다시 시도해주세요.");
        }
    }

    @Override
    @Transactional
    public FindTicketResponseDto updateTicketStatus(UUID ticketID,
        UpdateTicketStatusRequestDto requestDto) {
        try {

            Ticket ticket = ticketRepository.findById(ticketID).orElseThrow(TicketNotFoundException::new);

            TicketStatus newStatus = requestDto.status();
            log.info("newStatus : {}",newStatus.toString());

            // 상태가 이미 원하는 값이면 무시
            if (ticket.getStatus() == newStatus) {
                log.info("이미 처리된 메시지입니다. ticketId={}, status={}", ticket.getId(), ticket.getStatus());
                FindTicketResponseDto.of(ticket);
            }

            if (newStatus == TicketStatus.CONFIRM_PAYMENT) {
                List<UUID> seatIds = new ArrayList<>();
                seatIds.add(ticket.getSeatId());

                UpdateSeatStatusCommand command = new UpdateSeatStatusCommand(seatIds,
                    SeatStatus.RESERVED);
                seatClient.updateStatusInternal(command);
            }else{
                List<UUID> seatIds = new ArrayList<>();
                seatIds.add(ticket.getSeatId());

                UpdateSeatStatusCommand command = new UpdateSeatStatusCommand(seatIds,
                    SeatStatus.AVAILABLE);
                seatClient.updateStatusInternal(command);

            }
            ticket.updateTicketStatus(newStatus);
            return FindTicketResponseDto.of(ticket);
        }
        catch (ObjectOptimisticLockingFailureException e) {
            // 다른 트랜잭션이 먼저 상태를 변경한 경우 발생
            throw new IllegalStateException("다른 요청에서 이미 상태가 변경되었습니다. 다시 시도해주세요.");
        }
    }

    @Override
    @Transactional
    public FindTicketResponseDto expireTicket(UUID ticketId) {

        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);

            ticket.updateTicketStatus(TicketStatus.EXPIRED);

            return FindTicketResponseDto.of(ticket);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new IllegalStateException("다른 요청에서 티켓 상태가 변경되었습니다. 만료 처리가 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public DeleteTicketResponseDto deleteTicket(UUID ticketId) {

        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);

            ticket.updateTicketStatus(TicketStatus.CANCELLED);

            return DeleteTicketResponseDto.of(ticket);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new IllegalStateException("다른 요청에서 티켓 상태가 변경되었습니다. 삭제 처리에 실패했습니다.");
        }
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
