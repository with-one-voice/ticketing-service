package com.onevoice.ticket.application.client;

import com.onevoice.ticket.application.dto.SessionDetailsQuery;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "show-service")
public interface ShowClient {

    @GetMapping("/internal/{sessionId}")
    Optional<SessionDetailsQuery> getSessionDetail(@PathVariable("sessionId") UUID sessionId);
}
