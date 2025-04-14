package com.onevoice.notification.infrastructure.jpa;

import com.onevoice.notification.domain.Notification;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findAllByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);

    Optional<Notification> findByNotificationIdAndUserIdAndDeletedAtIsNull(UUID notificationId,
        UUID userId);
}
