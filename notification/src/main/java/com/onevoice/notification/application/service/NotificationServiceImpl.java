package com.onevoice.notification.application.service;

import com.onevoice.notification.application.client.UserClient;
import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.application.dto.message.EmailMessage;
import com.onevoice.notification.application.dto.query.FindNotificationQuery;
import com.onevoice.notification.application.dto.query.FindUserQuery;
import com.onevoice.notification.application.dto.query.ListNotificationQuery;
import com.onevoice.notification.application.event.EmailSendEvent;
import com.onevoice.notification.domain.Notification;
import com.onevoice.notification.domain.repository.NotificationRepository;
import com.onevoice.notification.exception.NotificationNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j(topic = "NotificationServiceImpl")
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserClient userClient;
    private final NotificationRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UUID create(CreateNotificationCommand command) {
        Notification notification = Notification.create(
            command.userId(),
            command.notificationType(),
            command.title(),
            command.message(),
            command.metadata()
        );

        Notification saved = repository.save(notification);
        addEvent(command);
        return saved.getNotificationId();
    }

    @Override
    public ListNotificationQuery reads(UUID userId, Pageable pageable) {
        List<FindNotificationQuery> queryList = repository.findAllByUserId(userId, pageable)
            .stream()
            .map(FindNotificationQuery::from)
            .toList();
        return new ListNotificationQuery(queryList);
    }

    @Override
    public FindNotificationQuery read(UUID userId, UUID notificationId) {
        return repository.findByUserIdAndNotificationId(userId, notificationId)
            .map(FindNotificationQuery::from)
            .orElseThrow(NotificationNotFoundException::new);
    }

    private void addEvent(CreateNotificationCommand command) {
        // notificationType 발송 이벤트
        switch (command.notificationType()) {
            case EMAIL: {
                FindUserQuery query = userClient.findUser(command.userId()).orElseThrow();
                EmailMessage message = new EmailMessage(
                    "username",
                    query.email(),
                    command.title(),
                    command.message(),
                    command.metadata()
                );
                eventPublisher.publishEvent(new EmailSendEvent(this, message));
                break;
            }
            case SMS:
                log.info("SMS event");
                break;
            case PUSH:
                log.info("PUSH event");
                break;
            default:
                log.info("Unknown notification type");
                break;
        }
        // 이벤트 성공 여부를 받아 notification status update
    }
}
