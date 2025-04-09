package com.onevoice.notification.infrastructure;

import com.onevoice.notification.domain.Notification;
import com.onevoice.notification.domain.NotificationStatus;
import com.onevoice.notification.domain.repository.NotificationRepository;
import com.onevoice.notification.infrastructure.jpa.NotificationJpaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Page<Notification> findAllByUserId(UUID userId, Pageable pageable) {
        // 실제 검색은 deletedAt is null
        return jpaRepository.findAllByUserIdAndDeletedAtIsNull(userId, pageable);
    }

    @Override
    public Optional<Notification> findByUserIdAndNotificationId(UUID userId, UUID notificationId) {
        return jpaRepository.findByUserIdAndNotificationIdAndDeletedAtIsNull(userId,
            notificationId);
    }

    @Override
    public Optional<Notification> findById(UUID notificationId) {
        return jpaRepository.findById(notificationId);
    }

    @Override
    public void updateStatus(UUID notificationId, NotificationStatus status) {

    }
}


