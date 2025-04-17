package com.onevoice.notification.infrastructure.message.handler;

import com.onevoice.notification.application.dto.event.NotificationEvent;
import com.onevoice.notification.domain.NotificationType;

public interface NotificationHandler {

    NotificationType supports();

    void handle(NotificationEvent event);
}
