package com.onevoice.notification.presentation;

import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.presentation.dto.request.CreateNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    public ResponseEntity<?> post(@RequestBody CreateNotificationRequest request) {
        return null;
    }

}
