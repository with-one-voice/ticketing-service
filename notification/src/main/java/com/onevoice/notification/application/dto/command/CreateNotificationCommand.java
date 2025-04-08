package com.onevoice.notification.application.dto.command;

import com.onevoice.notification.domain.NotificationType;
import java.util.UUID;

public record CreateNotificationCommand(
    UUID userId,
    NotificationType notificationType,
    String title,
    String message,
    String metadata
) {

}
