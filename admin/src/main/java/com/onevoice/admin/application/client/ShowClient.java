package com.onevoice.admin.application.client;


import com.onevoice.admin.presentation.dto.client.session.*;
import com.onevoice.admin.presentation.dto.client.show.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@FeignClient(name = "show-service")
public interface ShowClient {
    /*
     * show
     * */
    @PostMapping("/internal/shows")
    Optional<AdminCreateShowResponse> createShow(@RequestBody AdminCreateShowRequest request);

    @PatchMapping("/internal/shows/{showId}")
    Optional<AdminUpdateShowResponse> updateShow(
            @PathVariable UUID showId,
            @RequestBody AdminUpdateShowRequest request,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    );

    @GetMapping("/internal/shows/{showId}")
    AdminShowDetailResponse getShowDetail(
            @PathVariable UUID showId,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    );

    @GetMapping("/internal/shows")
    List<AdminShowResponse> getAllShows(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    );
    @DeleteMapping("/internal/shows/{showId}")
    void deleteShow(
            @PathVariable UUID showId,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    );

    /*
     * session
     * */
    @PostMapping("/internal/sessions/{showId}")
    Optional<AdminCreateSessionResponse> createSession(
            @PathVariable("showId") UUID showId,
            @RequestBody AdminCreateSessionRequest request,
            @RequestHeader("X-User-Id") UUID userId
    );

    @PatchMapping("/internal/sessions/{sessionId}")
    Optional<AdminUpdateSessionResponse> updateSession(
            @PathVariable UUID sessionId,
            @RequestBody AdminUpdateSessionRequest request,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    );

    @GetMapping("/internal/sessions/{sessionId}")
    AdminSessionDetailResponse getSessionDetail(
            @PathVariable UUID sessionId,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    );
    @GetMapping("/internal/sessions")
    List<AdminSessionResponse> getAllSessions(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    );

    @DeleteMapping("/internal/sessions/{sessionId}")
    void deleteSession(
            @PathVariable UUID sessionId,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    );
}
