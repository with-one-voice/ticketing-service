package com.onevoice.notification.infrastructure.message;

import com.onevoice.notification.application.dto.message.EmailContext;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.domain.NotificationStatus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j(topic = "EmailSender")
@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;
    private final EmailServerChecker emailServerChecker;
    private final NotificationService notificationService;

    public void send(EmailContext context) {
        try {
            // 메일 서버 상태 체크
            if (!emailServerChecker.isMailServerAvailable()) {
                log.error("Mail server is not available");
                updateEmailStatus(context.notificationId(), NotificationStatus.SERVER_ERROR);
                return;
            }

            SimpleMailMessage message = getSimpleMailMessage(context);
            javaMailSender.send(message);

            log.info("Email sent successfully to {}", context.email());
            updateEmailStatus(context.notificationId(), NotificationStatus.SENT);
        } catch (MailException e) {
            log.error("Failed to send email to {}: {}", context.email(), e.getMessage());
            handleEmailSendFailure(context);
        } catch (Exception e) {
            log.error("Unexpected error while sending email to {}: {}", context.email(),
                e.getMessage());
            updateEmailStatus(context.notificationId(), NotificationStatus.FAILED);
        }
    }

    private SimpleMailMessage getSimpleMailMessage(EmailContext context) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(context.title());
        message.setText(getText(context.username(), context.message()));
        message.setTo(context.email());
        return message;
    }

    private String getText(String username, String message) {
        return "to: " + username + "," + System.lineSeparator() + System.lineSeparator()
            + message + System.lineSeparator()
            + System.lineSeparator() + System.lineSeparator()
            + "from: " + System.lineSeparator() + "With One Voice";
    }

    // 이메일 발송 실패 시 처리
    private void handleEmailSendFailure(EmailContext context) {
        int retries = 3; // 재시도 횟수
        while (retries > 0) {
            try {
                SimpleMailMessage message = getSimpleMailMessage(context);

                javaMailSender.send(message);
                log.info("Email sent successfully to {} on retry", context.email());
                updateEmailStatus(context.notificationId(), NotificationStatus.SENT);
                return; // 성공했으면 메서드 종료

            } catch (MailException e) {
                log.error("Retry to send email failed to {}: {}", context.email(),
                    e.getMessage());
                retries--;
            }
        }
        // 모든 재시도 실패
        updateEmailStatus(context.notificationId(), NotificationStatus.FAILED);
    }

    private void updateEmailStatus(UUID notificationId, NotificationStatus status) {
        // 이메일 상태를 성공 또는 실패로 업데이트하는 로직
        log.info("Updated email status to {}", status);
        notificationService.updateStatus(notificationId, status);
    }
}
