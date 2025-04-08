package com.onevoice.notification.presentation.dto.request;

import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.domain.NotificationType;
import java.util.UUID;

public record CreateNotificationRequest(
    NotificationType notificationType,
    String title,
    String message,
    String metadata
) {

    public CreateNotificationCommand toCommand(UUID userId) {
        return new CreateNotificationCommand(
            userId,
            this.notificationType,
            this.title,
            this.message,
            this.metadata
        );
    }
}
