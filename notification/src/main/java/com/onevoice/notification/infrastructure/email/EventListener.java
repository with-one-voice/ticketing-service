package com.onevoice.notification.infrastructure.email;

import com.onevoice.notification.application.event.EmailSendEvent;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.domain.NotificationStatus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j(topic = "EventListener")
@Component
@RequiredArgsConstructor
public class EventListener implements ApplicationListener<EmailSendEvent> {

    private final JavaMailSender javaMailSender;
    private final NotificationService notificationService;

    @Override
    public void onApplicationEvent(EmailSendEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(event.getEmail().title());
            message.setText(getText(event.getEmail().username(), event.getEmail().message()));
            message.setTo(event.getEmail().email());
            javaMailSender.send(message);

            log.info("Email sent successfully to {}", event.getEmail().email());
            updateEmailStatus(event.getEmail().notificationId(), NotificationStatus.SENT);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", event.getEmail().email(), e.getMessage());
            updateEmailStatus(event.getEmail().notificationId(), NotificationStatus.FAILED);
        }
    }

    private String getText(String username, String message) {
        return "to: " + username + "," + System.lineSeparator() + System.lineSeparator()
            + message + System.lineSeparator()
            + System.lineSeparator() + System.lineSeparator()
            + "from: " + System.lineSeparator() + "With One Voice";
    }

    private void updateEmailStatus(UUID notificationId, NotificationStatus status) {
        // 이메일 상태를 성공 또는 실패로 업데이트하는 로직
        log.info("Updated email status to {}", status);
        notificationService.updateStatus(notificationId, status);
    }
}
