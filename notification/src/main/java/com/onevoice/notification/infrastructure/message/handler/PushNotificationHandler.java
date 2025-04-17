package com.onevoice.notification.infrastructure.message.handler;

import com.onevoice.notification.application.client.DiscordClient;
import com.onevoice.notification.application.dto.event.DiscordMessage;
import com.onevoice.notification.application.dto.event.NotificationEvent;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.domain.NotificationStatus;
import com.onevoice.notification.domain.NotificationType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushNotificationHandler implements NotificationHandler {

    private final DiscordClient discordClient;
    private final NotificationService notificationService;

    @Override
    public NotificationType supports() {
        return NotificationType.PUSH;
    }

    @Override
    public void handle(NotificationEvent event) {
        try {
            discordClient.push(getMessage(event));
            notificationService.updateStatus(event.notificationId(), NotificationStatus.SENT);
        } catch (Exception e) {
            log.error("Push notification failed", e);
            notificationService.updateStatus(event.notificationId(), NotificationStatus.FAILED);
        }
    }

    private DiscordMessage getMessage(NotificationEvent event) {
        return new DiscordMessage(
            event.title(), // 메인 메시지 설정
            "WOV Spring Boot", // 사용자 이름 설정
            null, // 아바타 URL 설정
            List.of(new DiscordMessage.Embed( // 유효한 임베드를 리스트로 추가
                event.message(),
                event.metadata(),
                "http://localhost:8080", // 유요한 형식의 url 이어야 한다. http:// or https://
                15258703 // 색상 값
            ))
        );
    }
}
