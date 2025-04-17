package com.onevoice.notification.infrastructure.message.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.application.dto.message.KafkaTopicMessage;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.domain.NotificationType;
import com.onevoice.notification.infrastructure.config.SecuredKafkaListener;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "SeatTopicConsumer")
@Component
@RequiredArgsConstructor
public class SeatTopicConsumer extends SecuredKafkaListener {

    private final NotificationService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "seat_confirm")
    public void seatConfirm(String message) {
        log.info("received seat confirm: {}", message);

        UUID userId = getUserId(message);
        CreateNotificationCommand command = new CreateNotificationCommand(
            userId,
            NotificationType.PUSH,
            "좌석 확정 알림",
            "요청하신 좌석이 확정되었습니다. 결제를 완료해주세요.",
            "topic: seat_confirm"
        );
        withSecurityContext(userId, () -> service.create(command));
    }

    @KafkaListener(topics = "seat_cancel")
    public void seatCancel(String message) {
        log.info("received seat cancel: {}", message);

        UUID userId = getUserId(message);
        CreateNotificationCommand command = new CreateNotificationCommand(
            userId,
            NotificationType.PUSH,
            "좌석 확정 실패 알림",
            "요청하신 좌석이 취소되었습니다.",
            "topic: seat_cancel"
        );
        withSecurityContext(userId, () -> service.create(command));
    }

    @KafkaListener(topics = "seat_expired")
    public void seatExpired(String message) {
        log.info("received seat expired: {}", message);

        UUID userId = getUserId(message);
        CreateNotificationCommand command = new CreateNotificationCommand(
            userId,
            NotificationType.PUSH,
            "좌석 선점 만료 알림",
            "요청하신 좌석의 선점 기한이 만료되었습니다.",
            "topic: seat_expired"
        );
        withSecurityContext(userId, () -> service.create(command));
    }

    private UUID getUserId(String message) {
        try {
            KafkaTopicMessage topicMessage = objectMapper.readValue(message,
                KafkaTopicMessage.class);
            return topicMessage.payload().userId();
        } catch (JsonProcessingException e) {
            log.error("failed to parse", e);
        }
        return null;
    }
}
