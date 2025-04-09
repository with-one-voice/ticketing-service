package com.onevoice.notification.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.common.dto.ResponseCode;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.presentation.dto.request.CreateNotificationRequest;
import java.net.URI;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j(topic = "NotificationController")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 알림 생성 API
     */
    @PostMapping
    public ResponseEntity<?> post(
        @RequestBody CreateNotificationRequest request,
        @AuthenticationPrincipal UUID userId
    ) {
        log.info("Request Body:{}", request);

        UUID notificationId = notificationService.create(request.toCommand(userId));
        URI location = UriComponentsBuilder.newInstance()
            .path("/api/notifications/{notificationId}")
            .buildAndExpand(notificationId)
            .toUri();
        return CommonResponse.of(ResponseCode.CREATED, location);
    }

    /**
     * 알림 목록 조회 API
     */
    @GetMapping
    public ResponseEntity<?> getList(
        @AuthenticationPrincipal UUID userId,
        Pageable pageable
    ) {
        log.info("Get list request: {}", pageable);
        // user 는 자신의 목록만 볼 수 있다.
        return CommonResponse.success(notificationService.reads(userId, pageable));
    }

    /**
     * 알림 조회 API
     */
    @GetMapping("/{notificationId}")
    public ResponseEntity<?> get(
        @PathVariable UUID notificationId,
        @AuthenticationPrincipal UUID userId
    ) {
        log.info("Get request: {}", notificationId);
        return CommonResponse.success(notificationService.read(userId, notificationId));
    }
}
