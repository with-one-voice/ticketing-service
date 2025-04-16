package com.onevoice.notification.application.event;

import com.onevoice.notification.application.client.UserClient;
import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.application.dto.message.EmailContext;
import com.onevoice.notification.application.dto.query.FindUserQuery;
import com.onevoice.notification.exception.NotificationUserNotFoundException;
import feign.FeignException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailNotificationEventPublisher implements NotificationEventPublisher {

    private final UserClient userClient;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean publish(UUID notificationId, CreateNotificationCommand command) {
        String emailAddress = findUserEmail(command.userId());
        if (emailAddress != null) {

            EmailContext context = new EmailContext(
                notificationId,
                emailAddress, // username
                emailAddress,
                command.title(),
                command.message(),
                command.metadata()
            );
            eventPublisher.publishEvent(new EmailSendEvent(this, context));
            return true;
        } else {
            // 이벤트 발행 실패
            log.info("Unknown email address");
            return false;
        }
    }

    private String findUserEmail(UUID userId) {
        try {
            return userClient.findUserById(userId)
                .map(FindUserQuery::email).orElseThrow(
                    NotificationUserNotFoundException::new);
        } catch (FeignException e) {
            log.error("Error finding user email", e);
            return null;
        }
    }
}
