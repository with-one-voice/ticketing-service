package com.onevoice.notification.presentation;

import com.onevoice.common.dto.CommonResponse;
import com.onevoice.common.dto.ResponseCode;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.presentation.dto.request.CreateNotificationRequest;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<?> post(@RequestBody CreateNotificationRequest request) {
        log.info("POST request: {}", request);
        // TODO: user_id 를 받아와 command 에 넣어야 한다.
        UUID notificationId = notificationService.create(request.toCommand(UUID.randomUUID()));
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
    public ResponseEntity<?> getList(Pageable pageable) {
        log.info("Get list request: {}", pageable);
        return CommonResponse.success(notificationService.reads(pageable));
    }

}
