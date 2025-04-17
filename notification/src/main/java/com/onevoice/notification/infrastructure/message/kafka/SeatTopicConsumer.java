package com.onevoice.notification.infrastructure.message.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.application.dto.message.KafkaTopicMessage;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.domain.NotificationType;
import com.onevoice.notification.exception.NotificationUserNotFoundException;
import com.onevoice.notification.infrastructure.config.SecuredKafkaListener;
import java.util.Optional;
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

        processTopicMessage(
            message,
            "seat_confirm",
            "좌석 확정 알림",
            "요청하신 좌석이 확정되었습니다. 결제를 완료해주세요."
        );
    }

    @KafkaListener(topics = "seat_cancel")
    public void seatCancel(String message) {
        log.info("received seat cancel: {}", message);

        processTopicMessage(
            message,
            "seat_cancel",
            "좌석 확정 실패 알림",
            "요청하신 좌석이 취소되었습니다."
        );
    }

    @KafkaListener(topics = "seat_expired")
    public void seatExpired(String message) {
        log.info("received seat expired: {}", message);

        processTopicMessage(
            message,
            "seat_expired",
            "좌석 선점 만료 알림",
            "요청하신 좌석의 선점 기한이 만료되었습니다."
        );
    }

    private void processTopicMessage(String message, String topic, String title, String content) {
        log.info("received {}: {}", topic, message);

        UUID userId = getUserId(message)
            .orElseThrow(NotificationUserNotFoundException::new);

        CreateNotificationCommand command = new CreateNotificationCommand(
            userId,
            NotificationType.PUSH,
            title,
            content,
            "topic: " + topic
        );
        withSecurityContext(userId, () -> service.create(command));
    }

    private Optional<UUID> getUserId(String message) {
        try {
            KafkaTopicMessage topicMessage = objectMapper.readValue(message,
                KafkaTopicMessage.class);
            return Optional.ofNullable(topicMessage.payload().userId());
        } catch (JsonProcessingException e) {
            log.error("메시지 파싱 실패: {}", message, e);
        }
        return Optional.empty();
    }
}
