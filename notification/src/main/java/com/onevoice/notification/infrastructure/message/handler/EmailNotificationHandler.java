package com.onevoice.notification.infrastructure.message.handler;

import com.onevoice.notification.application.client.UserClient;
import com.onevoice.notification.application.dto.event.NotificationEvent;
import com.onevoice.notification.application.dto.query.FindUserQuery;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.domain.NotificationStatus;
import com.onevoice.notification.domain.NotificationType;
import feign.FeignException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j(topic = "EmailNotificationHandler")
@Component
@RequiredArgsConstructor
public class EmailNotificationHandler implements NotificationHandler {

    private final UserClient userClient;
    private final JavaMailSender javaMailSender;
    private final EmailServerChecker emailServerChecker;
    private final NotificationService notificationService;

    // 각 구현체가 자신의 구현체를 반환하도록 하여 팩토리에서 타입을 따로 비교하지 않아도 된다.
    @Override
    public NotificationType supports() {
        return NotificationType.EMAIL;
    }

    @Override
    public void handle(NotificationEvent event) {
        String email = null;

        try {
            // get email address
            email = userClient.findUserById(event.userId())
                .map(FindUserQuery::email).orElseThrow();

            // 메일 서버 상태 체크
            if (!emailServerChecker.isMailServerAvailable()) {
                log.error("Mail server is not available");
                updateEmailStatus(event.notificationId(), NotificationStatus.SERVER_ERROR);
                return;
            }

            SimpleMailMessage message = getSimpleMailMessage(event, email);
            javaMailSender.send(message);

            log.info("Email sent successfully to {}", email);
            updateEmailStatus(event.notificationId(), NotificationStatus.SENT);
        } catch (FeignException e) {
            log.error("Error finding user email", e);
            updateEmailStatus(event.notificationId(), NotificationStatus.FAILED);
        } catch (MailException e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage());
            handleEmailSendFailure(event, email);
        } catch (Exception e) {
            log.error("Unexpected error while sending email to {}: {}", email,
                e.getMessage());
            updateEmailStatus(event.notificationId(), NotificationStatus.FAILED);
        }
    }

    private SimpleMailMessage getSimpleMailMessage(NotificationEvent event, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(event.title());
        message.setText(getText(email, event.message()));
        message.setTo(email);
        return message;
    }

    private String getText(String username, String message) {
        return "to: " + username + "," + System.lineSeparator() + System.lineSeparator()
            + message + System.lineSeparator()
            + System.lineSeparator() + System.lineSeparator()
            + "from: " + System.lineSeparator() + "With One Voice";
    }

    // 이메일 발송 실패 시 처리
    private void handleEmailSendFailure(NotificationEvent event, String email) {
        int retries = 3; // 재시도 횟수
        while (retries > 0) {
            try {
                SimpleMailMessage message = getSimpleMailMessage(event, email);

                javaMailSender.send(message);
                log.info("Email sent successfully to {} on retry", email);
                updateEmailStatus(event.notificationId(), NotificationStatus.SENT);
                return; // 성공했으면 메서드 종료

            } catch (MailException e) {
                log.error("Retry to send email failed to {}: {}", email,
                    e.getMessage());
                retries--;
            }
        }
        // 모든 재시도 실패
        updateEmailStatus(event.notificationId(), NotificationStatus.FAILED);
    }

    private void updateEmailStatus(UUID notificationId, NotificationStatus status) {
        // 이메일 상태를 성공 또는 실패로 업데이트하는 로직
        log.info("Updated email status to {}", status);
        notificationService.updateStatus(notificationId, status);
    }
}
