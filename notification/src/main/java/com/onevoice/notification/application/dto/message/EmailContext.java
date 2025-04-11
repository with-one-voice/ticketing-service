package com.onevoice.notification.application.dto.message;

import java.util.UUID;

public record EmailContext(
    UUID notificationId,
    String username,
    String email,
    String title,
    String message,
    String metadata
) {

}
