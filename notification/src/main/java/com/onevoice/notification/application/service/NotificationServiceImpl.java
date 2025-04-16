package com.onevoice.notification.application.service;

import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.application.dto.event.NotificationEvent;
import com.onevoice.notification.application.dto.query.FindNotificationQuery;
import com.onevoice.notification.application.dto.query.ListNotificationQuery;
import com.onevoice.notification.domain.Notification;
import com.onevoice.notification.domain.NotificationStatus;
import com.onevoice.notification.domain.repository.NotificationRepository;
import com.onevoice.notification.exception.NotificationNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "NotificationServiceImpl")
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UUID create(CreateNotificationCommand command) {
        //  엔티티에 객체 생성에 대한 책임을 부여한다.
        Notification notification = Notification.create(
            command.userId(),
            command.notificationType(),
            command.title(),
            command.message(),
            command.metadata()
        );

        Notification saved = repository.save(notification);

        eventPublisher.publishEvent(NotificationEvent.of(saved.getNotificationId(), command));

        return saved.getNotificationId();
    }

    @Override
    @Transactional(readOnly = true)
    public ListNotificationQuery reads(UUID userId, Pageable pageable) {
        List<FindNotificationQuery> queryList = repository.findAllByUserId(userId, pageable)
            .stream()
            .map(FindNotificationQuery::from)
            .toList();

        return new ListNotificationQuery(queryList);
    }

    @Override
    @Transactional(readOnly = true)
    public FindNotificationQuery read(UUID notificationId, UUID userId) {
        return repository.findByIdAndUserId(notificationId, userId)
            .map(FindNotificationQuery::from)
            .orElseThrow(NotificationNotFoundException::new);
    }

    @Override
    public void updateStatus(UUID notificationId, NotificationStatus status) {
        Notification notification = repository.findByNotificationId(notificationId)
            .orElseThrow(NotificationNotFoundException::new);
        notification.updateNotificationStatus(status);
    }

    @Override
    public void delete(UUID notificationId) {
        Notification notification = repository.findByNotificationId(notificationId)
            .orElseThrow(NotificationNotFoundException::new);
        notification.delete(notificationId);
    }
}
