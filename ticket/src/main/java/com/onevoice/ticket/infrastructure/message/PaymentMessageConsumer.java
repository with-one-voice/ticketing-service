package com.onevoice.ticket.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.ticket.application.dto.message.PaymentFailMessage;
import com.onevoice.ticket.application.dto.message.PaymentCreateMessage;
import com.onevoice.ticket.application.dto.message.PaymentSuccessMessage;
import com.onevoice.ticket.application.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMessageConsumer {

    private final ObjectMapper objectMapper;
    private final TicketService ticketService;

    @KafkaListener(topics = "payment_create", groupId = "ticket-group")
    public void consumePaymentCreate(String messageJson) {
        try {

            JsonNode root = objectMapper.readTree(messageJson);
            JsonNode payload = root.get("payload");

            PaymentCreateMessage message = objectMapper.treeToValue(payload,
                PaymentCreateMessage.class);

            log.info("[payment_create] Received :{}", message);

            ticketService.confirmTicketAfterPayment(message.ticketId());

        } catch (Exception e) {
            log.error("Failed to process payment_success message", e);
        }
    }

    @KafkaListener(topics = "payment_success", groupId = "ticket-group")
    public void consumePaymentSuccess(String messageJson) {
        try {
            JsonNode root = objectMapper.readTree(messageJson);
            JsonNode payload = root.get("payload");

            PaymentSuccessMessage message = objectMapper.treeToValue(payload,
                PaymentSuccessMessage.class);

            log.info("[payment_success] Received :{}",message);

        } catch (Exception e) {
            log.error("Failed to process payment_success message", e);
        }
    }

    @KafkaListener(topics = "payment_fail", groupId = "ticket-group")
    public void consumePaymentFail(String messageJson) {

        try {
            JsonNode root = objectMapper.readTree(messageJson);
            JsonNode payload = root.get("payload");

            PaymentFailMessage message = objectMapper.treeToValue(payload,
                PaymentFailMessage.class);

            log.info("[payment_fail] Received :{}", message);
            ticketService.failTicketAfterPayment(message.ticketId());

        } catch (JsonProcessingException e) {
            log.error("Failed to process payment_fail message");
        }

    }
}
