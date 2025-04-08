package com.onevoice.notification.domain.repository;

import com.onevoice.notification.domain.Notification;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(UUID notificationId);
}
