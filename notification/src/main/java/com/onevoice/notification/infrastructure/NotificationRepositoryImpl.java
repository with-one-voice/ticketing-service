package com.onevoice.notification.infrastructure;

import com.onevoice.notification.domain.Notification;
import com.onevoice.notification.domain.repository.NotificationRepository;
import com.onevoice.notification.infrastructure.jpa.NotificationJpaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
    public Optional<Notification> findById(UUID notificationId) {
        return jpaRepository.findById(notificationId);
    }
}


