package com.onevoice.notification.application.dto.message;

public record EmailMessage(
    String username,
    String email,
    String title,
    String message,
    String metadata
) {

}
