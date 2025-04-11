package com.onevoice.payment.application.client;

import com.onevoice.common.config.FeignHttpConfig;
import com.onevoice.payment.application.dto.client.FindTicketResponseDto;
import com.onevoice.payment.application.dto.client.UpdateTicketStatusRequestDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ticket-service", url = "localhost:8005", configuration = FeignHttpConfig.class)
public interface TicketClient {

    @PatchMapping("/internal/{ticketId}")
    Optional<FindTicketResponseDto> updateTicketStatus(
        @PathVariable UUID ticketId,
        @RequestBody UpdateTicketStatusRequestDto requestDto
    );
}
