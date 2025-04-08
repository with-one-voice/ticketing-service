package com.onevoice.notification.application.service;

import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.application.dto.query.FindNotificationQuery;
import com.onevoice.notification.application.dto.query.ListNotificationQuery;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    UUID create(CreateNotificationCommand command);

    ListNotificationQuery reads(UUID userId, Pageable pageable);

    FindNotificationQuery read(UUID userId, UUID notificationId);
}
