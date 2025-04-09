package com.onevoice.notification.application.dto.message;

import java.util.UUID;

public record EmailMessage(
    UUID notificationId,
    String username,
    String email,
    String title,
    String message,
    String metadata
) {

}
