package com.onevoice.notification.application.service;

import com.onevoice.notification.application.client.UserClient;
import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.application.dto.message.EmailContext;
import com.onevoice.notification.application.dto.query.FindNotificationQuery;
import com.onevoice.notification.application.dto.query.FindUserQuery;
import com.onevoice.notification.application.dto.query.ListNotificationQuery;
import com.onevoice.notification.application.event.EmailSendEvent;
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
public class NotificationServiceImpl implements NotificationService {

    private final UserClient userClient;
    private final NotificationRepository repository;
    private final ApplicationEventPublisher publisher;

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

        // save 를 명시적으로 호출해야 해서 @Transactional 을 제외했다.
        Notification saved = repository.save(notification);
        publishEvent(command, saved.getNotificationId());
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
    @Transactional
    public void updateStatus(UUID notificationId, NotificationStatus status) {
        Notification notification = repository.findByNotificationId(notificationId)
            .orElseThrow(NotificationNotFoundException::new);
        notification.updateNotificationStatus(status);
    }

    @Override
    @Transactional
    public void delete(UUID notificationId) {
        Notification notification = repository.findByNotificationId(notificationId)
            .orElseThrow(NotificationNotFoundException::new);
        notification.delete(notificationId);
    }

    private void publishEvent(CreateNotificationCommand command, UUID notificationId) {
        // notificationType 발송 이벤트 발행
        switch (command.notificationType()) {
            case EMAIL: {
                FindUserQuery query = userClient.findUserById(command.userId()).orElseThrow();
                EmailContext message = new EmailContext(
                    notificationId,
                    "WOV", // username 이 없어서 임의로 넣음
                    query.email(),
                    command.title(),
                    command.message(),
                    command.metadata()
                );
                // TODO: 트랜잭션과 상관 없으니 @Async 로 변경하자.
                publisher.publishEvent(new EmailSendEvent(this, message));
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
    }
}
