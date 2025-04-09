package com.onevoice.notification.domain.repository;

import com.onevoice.notification.domain.Notification;
import com.onevoice.notification.domain.NotificationStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepository {

    Notification save(Notification notification);

    Page<Notification> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Notification> findByUserIdAndNotificationId(UUID userId, UUID notificationId);

    Optional<Notification> findById(UUID notificationId);

    void updateStatus(UUID notificationId, NotificationStatus status);
}
