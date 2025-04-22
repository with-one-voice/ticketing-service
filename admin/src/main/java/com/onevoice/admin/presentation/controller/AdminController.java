package com.onevoice.admin.presentation.controller;

import com.onevoice.admin.application.service.AdminService;
import com.onevoice.admin.presentation.dto.client.session.*;
import com.onevoice.admin.presentation.dto.client.show.*;
import com.onevoice.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AdminController {


    private final AdminService adminService;

    /*
    * show
    * */
    @PostMapping
    public Optional<AdminCreateShowResponse> createShow(
            @RequestBody AdminCreateShowRequest request) {
        return adminService.createShow(request);
    }
    @PutMapping("/shows/{showId}")
    public ResponseEntity<CommonResponse<AdminUpdateShowResponse>> updateShow(
            @PathVariable UUID showId,
            @RequestBody AdminUpdateShowRequest request,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    ) {
        AdminUpdateShowResponse response = adminService.updateShow(showId, request, userId, role)
                .orElseThrow(() -> new IllegalStateException("공연 수정 실패"));
        return CommonResponse.success(response);
    }

    @GetMapping("/shows/{showId}")
    public ResponseEntity<CommonResponse<AdminShowDetailResponse>> getShowDetail(
            @PathVariable UUID showId,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    ) {
        AdminShowDetailResponse response = adminService.getShowDetail(showId, userId, role);
        return CommonResponse.success(response);
    }

    @GetMapping("/shows")
    public ResponseEntity<CommonResponse<List<AdminShowResponse>>> getAllShows(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    ) {
        List<AdminShowResponse> shows = adminService.getAllShows(userId, role);
        return CommonResponse.success(shows);
    }
    @DeleteMapping("/shows/{showId}")
    public ResponseEntity<CommonResponse<String>> deleteShow(
            @PathVariable UUID showId,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    ) {
        adminService.deleteShow(showId, userId, role);
        return CommonResponse.success("공연 삭제가 완료되었습니다.");
    }

    /*
    * session
    * */
    @PostMapping("/sessions/{showId}")
    public Optional<AdminCreateSessionResponse> createSession(
            @PathVariable UUID showId,
            @RequestBody AdminCreateSessionRequest request,
            @AuthenticationPrincipal UUID userId
    ) {
        return adminService.createSession(showId, request, userId);
    }
    @PutMapping("/sessions/{sessionId}")
    public ResponseEntity<CommonResponse<AdminUpdateSessionResponse>> updateSession(
            @PathVariable UUID sessionId,
            @RequestBody AdminUpdateSessionRequest request,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    ) {
        AdminUpdateSessionResponse response = adminService.updateSession(sessionId, request, userId, role)
                .orElseThrow(() -> new IllegalStateException("회차 수정 실패"));
        return CommonResponse.success(response);
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<CommonResponse<AdminSessionDetailResponse>> getSessionDetail(
            @PathVariable UUID sessionId,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    ) {
        AdminSessionDetailResponse response = adminService.getSessionDetail(sessionId, userId, role);
        return CommonResponse.success(response);
    }

    @GetMapping("/sessions")
    public ResponseEntity<CommonResponse<List<AdminSessionResponse>>> getAllSessions(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    ) {
        List<AdminSessionResponse> sessions = adminService.getAllSessions(userId, role);
        return CommonResponse.success(sessions);
    }
    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<CommonResponse<String>> deleteSession(
            @PathVariable UUID sessionId,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-User-Role") String role
    ) {
        adminService.deleteSession(sessionId, userId, role);
        return CommonResponse.success("공연 회차 삭제가 완료되었습니다.");
    }
}
