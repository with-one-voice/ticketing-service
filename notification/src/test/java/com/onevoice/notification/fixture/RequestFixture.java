package com.onevoice.notification.fixture;

import com.onevoice.notification.application.dto.query.FindNotificationQuery;
import com.onevoice.notification.domain.NotificationStatus;
import com.onevoice.notification.domain.NotificationType;
import com.onevoice.notification.presentation.dto.request.CreateNotificationRequest;
import java.time.LocalDateTime;
import java.util.UUID;

public class RequestFixture {

    public static CreateNotificationRequest validRequest() {
        return new CreateNotificationRequest(
            NotificationType.EMAIL,
            "title",
            "message",
            "metadata"
        );
    }

    public static CreateNotificationRequest invalidRequest() {
        return new CreateNotificationRequest(
            null,
            "title",
            "",
            "metadata"
        );
    }

    public static FindNotificationQuery createFindQuery(UUID id) {
        return new FindNotificationQuery(
            id,
            NotificationType.EMAIL,
            NotificationStatus.PENDING,
            "title",
            "message",
            "metadata",
            LocalDateTime.now()
        );
    }
}
