package com.onevoice.notification.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.notification.application.dto.command.CreateNotificationCommand;
import com.onevoice.notification.application.service.NotificationService;
import com.onevoice.notification.domain.NotificationType;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j(topic = "NotificationEmailConsumer")
@Component
@RequiredArgsConstructor
public class NotificationEmailConsumer {

    private final NotificationService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "notification_email")
    public void listen(String message) {

        log.info("Received message: {}", message);
        try {
            JsonNode node = objectMapper.readTree(message);
            UUID userId = UUID.fromString(node.get("userId").asText());
            CreateNotificationCommand command = new CreateNotificationCommand(
                userId,
                NotificationType.EMAIL,
                "[WOV] 결제하신 내역입니다.",
                getMessage(node),
                getMetadata(node)
            );
            // AuditAware 적용, FeignClient 호출을 위한 임시 Security 컨텍스트
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"));
            context.setAuthentication(
                new UsernamePasswordAuthenticationToken(userId, null, authorities));
            SecurityContextHolder.setContext(context);
            service.create(command);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        } finally {
            // Security Context 정리
            SecurityContextHolder.clearContext();
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
