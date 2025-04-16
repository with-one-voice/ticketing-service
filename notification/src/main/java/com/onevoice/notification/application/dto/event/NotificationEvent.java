package com.onevoice.notification.application.dto.event;

import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.domain.NotificationType;
import java.util.UUID;

public record NotificationEvent(
    UUID notificationId,
    UUID userId,
    NotificationType notificationType,
    String title,
    String message,
    String metadata
) {

    public static NotificationEvent of(
        UUID notificationId,
        CreateNotificationCommand command
    ) {
        return new NotificationEvent(
            notificationId,
            command.userId(),
            command.notificationType(),
            command.title(),
            command.message(),
            command.metadata()
        );
    }
}
