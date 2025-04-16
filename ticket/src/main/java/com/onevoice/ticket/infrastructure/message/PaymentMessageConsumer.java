package com.onevoice.ticket.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.ticket.application.dto.PaymentFailMessage;
import com.onevoice.ticket.application.dto.PaymentSuccessMessage;
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

    @KafkaListener(topics = "payment_success", groupId = "ticket-group")
    public void consumePaymentSuccess(String messageJson) {
        try {
            PaymentSuccessMessage message = objectMapper.readValue(messageJson,
                PaymentSuccessMessage.class);

            log.info("[payment_success] Received :{}", message);

            ticketService.confirmTicketAfterPayment(message.ticketId());

        } catch (Exception e) {
            log.error("Failed to process payment_success message", e);
        }
    }

    @KafkaListener(topics = "payment_fail", groupId = "ticket-group")
    public void consumePaymentFail(String messageJson){
        try{
            PaymentFailMessage message = objectMapper.readValue(messageJson,
                PaymentFailMessage.class);

            log.info("[payment_fail] Received :{}", message);
            ticketService.failTicketAfterPayment(message.ticketId());

        }catch (JsonProcessingException e){
            log.error("Failed to process payment_fail message");
        }

    }
}
