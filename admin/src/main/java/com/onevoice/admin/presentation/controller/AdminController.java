package com.onevoice.admin.presentation.controller;

import com.onevoice.admin.application.service.AdminService;
import com.onevoice.admin.presentation.dto.client.AdminCreateSessionRequest;
import com.onevoice.admin.application.dto.AdminCreateSessionResponse;
import com.onevoice.admin.presentation.dto.client.AdminCreateShowRequest;
import com.onevoice.admin.application.dto.AdminCreateShowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping
    public Optional<AdminCreateShowResponse> createShow(@RequestBody AdminCreateShowRequest request) {
        return adminService.createShow(request);
    }
    @PostMapping("/sessions/{showId}")
    public Optional<AdminCreateSessionResponse> createSession(
            @PathVariable UUID showId,
            @RequestBody AdminCreateSessionRequest request,
            @AuthenticationPrincipal UUID userId
    ) {
        return adminService.createSession(showId, request, userId);
    }



}
