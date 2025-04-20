package com.onevoice.notification.presentation.dto.request;

import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.domain.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateNotificationRequest(
    @NotNull(message = "메시지 타입을 올바르게 지정해 주세요. EMAIL | SMS | PUSH")
    NotificationType notificationType,

    String title,

    @NotBlank(message = "메시지 내용을 입력해 주세요.")
    String message,

    String metadata
) {

    public CreateNotificationCommand to(UUID userId) {
        return new CreateNotificationCommand(
            userId,
            this.notificationType,
            this.title,
            this.message,
            this.metadata
        );
    }
}
