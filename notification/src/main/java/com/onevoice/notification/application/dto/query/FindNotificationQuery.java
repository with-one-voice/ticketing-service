package com.onevoice.notification.application.dto.query;

import com.onevoice.notification.domain.Notification;
import com.onevoice.notification.domain.NotificationStatus;
import com.onevoice.notification.domain.NotificationType;
import java.time.LocalDateTime;
import java.util.UUID;

public record FindNotificationQuery(
    UUID notificationId,
    NotificationType notificationType,
    NotificationStatus notificationStatus,
    String title,
    String message,
    String metadata,
    LocalDateTime createdAt
) {

    public static FindNotificationQuery from(Notification notification) {
        return new FindNotificationQuery(
            notification.getNotificationId(),
            notification.getNotificationType(),
            notification.getNotificationStatus(),
            notification.getTitle(),
            notification.getMessage(),
            notification.getMetadata(),
            notification.getCreatedAt()
        );
    }
}
