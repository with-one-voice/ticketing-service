package com.onevoice.notification.domain.repository;

import com.onevoice.notification.domain.Notification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface NotificationRepository {

    Notification save(Notification notification);

    List<Notification> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Notification> findByUserIdAndNotificationId(UUID userId, UUID notificationId);
}
