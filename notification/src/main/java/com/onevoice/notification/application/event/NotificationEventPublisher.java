package com.onevoice.notification.application.event;

import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import java.util.UUID;

public interface NotificationEventPublisher {

    boolean publish(UUID notificationId, CreateNotificationCommand command);
}
