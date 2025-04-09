package com.onevoice.notification.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.common.dto.ResponseCode;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.presentation.dto.request.CreateNotificationRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        @RequestBody @Valid CreateNotificationRequest request,
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
        log.info("Get list request: {} {}", userId, pageable);
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

    /**
     * 알림 삭제 API
     */
//    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> delete(@PathVariable UUID notificationId) {
        log.info("Delete request: {}", notificationId);
        //  관리자만 삭제할 수 있다. 현재는 테스트를 위해 권한 주석 처리
        notificationService.delete(notificationId);
        return CommonResponse.of(ResponseCode.NOTIFICATION_NO_CONTENT, "삭제 완료");
    }
}
