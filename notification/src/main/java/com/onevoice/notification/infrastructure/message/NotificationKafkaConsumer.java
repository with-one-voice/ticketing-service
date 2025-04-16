package com.onevoice.notification.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.notification.infrastructure.config.SecuredKafkaListener;
import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.domain.NotificationType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "NotificationEmailConsumer")
@Component
@RequiredArgsConstructor
public class NotificationKafkaConsumer extends SecuredKafkaListener {

    private final NotificationService service;
    private final ObjectMapper objectMapper;

    // 토픽에서 발행하는 내용에 notification type 을 지정
    @KafkaListener(topics = "notification_send")
    public void consumer(String message) {
        log.info("Received message: {}", message);

        try {
            JsonNode node = objectMapper.readTree(message);
            UUID userId = UUID.fromString(node.get("userId").asText());
            NotificationType type = NotificationType.valueOf(node.get("type").asText());
            CreateNotificationCommand command = new CreateNotificationCommand(
                userId,
                type,
                "[WOV] 결제하신 내역입니다.",
                getMessage(node),
                getMetadata(node)
            );

            // security context 세팅 후 서비스 로직 실행
            withSecurityContext(userId, () -> service.create(command));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String getMessage(JsonNode payload) {
        return "주문하신 ticket: " + payload.get("ticketId").asText() + System.lineSeparator() +
            payload.get("paymentStatus").asText() + System.lineSeparator();
    }

    private String getMetadata(JsonNode payload) {
        return "paymentId: " + payload.get("paymentId").asText();
    }
}
