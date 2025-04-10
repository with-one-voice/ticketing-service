package com.onevoice.ticket.presentation;

import com.onevoice.ticket.application.service.TicketService;
import com.onevoice.ticket.presentation.dto.request.UpdateTicketStatusRequestDto;
import com.onevoice.ticket.presentation.dto.response.FindTicketResponseDto;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class TicketInternalController {

    private final TicketService ticketService;

    @PatchMapping("/{ticketId}")
    public Optional<FindTicketResponseDto> updateTicketStatus(@PathVariable UUID ticketId, @AuthenticationPrincipal UUID userId,
        @RequestBody UpdateTicketStatusRequestDto requestDto) {
        FindTicketResponseDto responseDto = ticketService.updateTicketStatus(ticketId, userId,requestDto);
        return Optional.ofNullable(responseDto);
    }

}
