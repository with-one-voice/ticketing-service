package com.onevoice.notification.domain.repository;

import com.onevoice.notification.domain.Notification;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepository {

    Notification save(Notification notification);

    Page<Notification> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Notification> findByIdAndUserId(UUID notificationId, UUID userId);

    Optional<Notification> findByNotificationId(UUID notificationId);
}
