package com.onevoice.notification.application.service;

import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.application.dto.query.ListNotificationQuery;
import com.onevoice.notification.domain.Notification;
import com.onevoice.notification.domain.repository.NotificationRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public UUID create(CreateNotificationCommand command) {
        // TODO: email 전송 이벤트 발행해야 한다.

        Notification notification = Notification.create(
            command.userId(),
            command.notificationType(),
            command.title(),
            command.message(),
            command.metadata()
        );

        Notification saved = repository.save(notification);
        return saved.getNotificationId();
    }

    @Override
    public ListNotificationQuery reads(Pageable pageable) {
        return null;
    }
}
