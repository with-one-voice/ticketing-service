package com.onevoice.admin.application.client;


import com.onevoice.admin.application.dto.AdminCreateSessionResponse;
import com.onevoice.admin.application.dto.AdminCreateShowResponse;
import com.onevoice.admin.presentation.dto.client.AdminCreateSessionRequest;
import com.onevoice.admin.presentation.dto.client.AdminCreateShowRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "show-service")
public interface ShowClient {
    @PostMapping("/internal/shows")
    Optional<AdminCreateShowResponse> createShow(@RequestBody AdminCreateShowRequest request);

    @PostMapping("/internal/sessions/{showId}")
    Optional<AdminCreateSessionResponse> createSession(
            @PathVariable("showId") UUID showId,
            @RequestBody AdminCreateSessionRequest request,
            @RequestHeader("X-User-Id") UUID userId
    );
}
