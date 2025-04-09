package com.onevoice.notification.infrastructure;

import com.onevoice.notification.domain.Notification;
import com.onevoice.notification.domain.repository.NotificationRepository;
import com.onevoice.notification.infrastructure.jpa.NotificationJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;

    @Override
    public Notification save(Notification notification) {
        return jpaRepository.save(notification);
    }

    @Override
    public List<Notification> findAllByUserId(UUID userId, Pageable pageable) {
        return jpaRepository.findAllByUserIdAndDeletedAtIsNotNull(userId, pageable);
    }

    @Override
    public Optional<Notification> findByUserIdAndNotificationId(UUID userId, UUID notificationId) {
        return jpaRepository.findByUserIdAndNotificationIdAndDeletedAtIsNotNull(userId,
            notificationId);
    }
}


