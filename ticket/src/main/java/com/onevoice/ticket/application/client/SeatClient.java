package com.onevoice.ticket.application.client;

import com.onevoice.ticket.application.dto.FindHoldSeatQuery;
import com.onevoice.ticket.application.dto.FindSeatQuery;
import com.onevoice.ticket.application.dto.HoldSeatCommand;
import com.onevoice.ticket.application.dto.UpdateSeatStatusCommand;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "seat-service")
public interface SeatClient {

    @PutMapping("/internal/{sessionId}/hold")
    Optional<FindHoldSeatQuery> holdSeatsInternal(
        @PathVariable UUID sessionId,
        @RequestBody HoldSeatCommand command
    );

    @PutMapping("/internal/status")
    Optional<List<FindSeatQuery>> updateStatusInternal(
        @RequestBody UpdateSeatStatusCommand command
    );
}
