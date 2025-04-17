package com.onevoice.ticket.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.ticket.application.dto.message.PaymentFailMessage;
import com.onevoice.ticket.application.dto.message.PaymentCreateMessage;
import com.onevoice.ticket.application.dto.message.PaymentSuccessMessage;
import com.onevoice.ticket.application.dto.message.SeatExpiredMessage;
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
    public void consumePaymentCreate(String messageJson) throws JsonProcessingException {

        JsonNode root = objectMapper.readTree(messageJson);
        JsonNode payload = root.get("payload");

        PaymentCreateMessage message = objectMapper.treeToValue(payload,
            PaymentCreateMessage.class);

        log.info("[payment_create] Received :{}", message);

        ticketService.confirmTicketAfterPayment(message.ticketId());
    }

    @KafkaListener(topics = "payment_success", groupId = "ticket-group")
    public void consumePaymentSuccess(String messageJson) throws JsonProcessingException {

        JsonNode root = objectMapper.readTree(messageJson);
        JsonNode payload = root.get("payload");

        PaymentSuccessMessage message = objectMapper.treeToValue(payload,
            PaymentSuccessMessage.class);

        log.info("[payment_success] Received :{}", message);

        ticketService.updateTicketStatusAfterPaymentSuccess(message.ticketId());
    }

    @KafkaListener(topics = "payment_fail", groupId = "ticket-group")
    public void consumePaymentFail(String messageJson) throws JsonProcessingException {

        JsonNode root = objectMapper.readTree(messageJson);
        JsonNode payload = root.get("payload");

        PaymentFailMessage message = objectMapper.treeToValue(payload,
            PaymentFailMessage.class);

        log.info("[payment_fail] Received :{}", message);
        ticketService.failTicketAfterPayment(message.ticketId());
    }

    @KafkaListener(topics = "seat_expired", groupId = "ticket-group")
    public void consumeSeatExpired(String messageJson) throws JsonProcessingException {

        JsonNode root = objectMapper.readTree(messageJson);
        JsonNode payload = root.get("payload");

        SeatExpiredMessage message = objectMapper.treeToValue(payload,
            SeatExpiredMessage.class);

        ticketService.expiredTicketAfterPayment(message.seatIds(), message.userId());
    }
}
