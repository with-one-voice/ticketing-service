package com.onevoice.ticket.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.ticket.application.service.TicketService;
import com.onevoice.ticket.presentation.dto.request.CreateTicketRequestDto;
import com.onevoice.ticket.presentation.dto.request.UpdateTicketStatusRequestDto;
import com.onevoice.ticket.presentation.dto.response.CreateTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.DeleteTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.FindTicketResponseDto;
import com.onevoice.ticket.presentation.dto.response.ListReservedTicketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<CommonResponse<CreateTicketResponseDto>> createTicket(@RequestBody CreateTicketRequestDto requestDto) {

        CreateTicketResponseDto responseDto = ticketService.createTicket(requestDto);
        return CommonResponse.success(responseDto);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Page<ListReservedTicketResponseDto>>> getReservedTickets(
            @AuthenticationPrincipal UUID userID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc
                                                                                                  ) {

        Page<ListReservedTicketResponseDto> responseDto = ticketService.getReservedTickets(userID,page,size,sortBy,isAsc);
        return CommonResponse.success(responseDto);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<CommonResponse<FindTicketResponseDto>> getTicket(@PathVariable UUID ticketId, @AuthenticationPrincipal UUID userId) {
        FindTicketResponseDto responseDto = ticketService.getTicket(ticketId, userId);
        return CommonResponse.success(responseDto);
    }

    @PatchMapping("/{ticketId}")
    public ResponseEntity<CommonResponse<FindTicketResponseDto>> updateTicketStatus(@PathVariable UUID ticketId, @AuthenticationPrincipal UUID userId,
                                                                                    @RequestBody UpdateTicketStatusRequestDto requestDto) {
        FindTicketResponseDto responseDto = ticketService.updateTicketStatus(ticketId, userId,requestDto);
        return CommonResponse.success(responseDto);
    }

    @PatchMapping("/{ticketId}/expiration")
    public ResponseEntity<CommonResponse<FindTicketResponseDto>> expireTicket(@PathVariable UUID ticketId) {
        FindTicketResponseDto responseDto = ticketService.expireTicket(ticketId);
        return CommonResponse.success(responseDto);
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<CommonResponse<DeleteTicketResponseDto>> deleteTicket(@PathVariable UUID ticketId) {
        DeleteTicketResponseDto responseDto = ticketService.deleteTicket(ticketId);
        return CommonResponse.success(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<Page<ListReservedTicketResponseDto>>> searchTickets(
            @AuthenticationPrincipal UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean isAsc,
            @RequestParam String keyword
    ) {

        Page<ListReservedTicketResponseDto> responseDto = ticketService.searchTickets(userId, page, size, sortBy, isAsc, keyword);
        return CommonResponse.success(responseDto);
    }
}
