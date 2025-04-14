package com.onevoice.ticket.application.client;

import com.onevoice.ticket.application.dto.FindUserQuery;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/{userId}")
    Optional<FindUserQuery> findUserById(@PathVariable UUID userId);
}
