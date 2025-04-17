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

@Slf4j(topic = "TicketTopicConsumer")
@Component
@RequiredArgsConstructor
public class TicketTopicConsumer extends SecuredKafkaListener {

    private final NotificationService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "ticket_status")
    public void ticketStatus(String message) {
        log.info("received ticket status: {}", message);

        UUID userId = getUserId(message);
        CreateNotificationCommand command = new CreateNotificationCommand(
            userId,
            NotificationType.PUSH,
            "티켓 확정 알림",
            "결제하신 티켓이 확정되었습니다. 마이페이지를 확인해주세요.",
            "topic: ticket_status"
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
