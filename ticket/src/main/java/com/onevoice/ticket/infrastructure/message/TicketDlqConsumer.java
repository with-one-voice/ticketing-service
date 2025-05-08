package com.onevoice.ticket.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.ticket.application.dto.message.PaymentCreateMessage;
import com.onevoice.ticket.application.dto.message.PaymentFailMessage;
import com.onevoice.ticket.application.dto.message.PaymentSuccessMessage;
import com.onevoice.ticket.application.dto.message.SeatExpiredMessage;
import com.onevoice.ticket.application.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketDlqConsumer {

    private final ObjectMapper objectMapper;

    /**
     * 현재는 log만 기록했는데, Slack이나 이메일 등을 통해 알림을 설정하는 것도 좋아 보입니다.
     *
     */

    @KafkaListener(topics = "payment_create.DLQ", groupId = "ticket-dlq-group")
    public void consumePaymentCreateDLQ(ConsumerRecord<String,String> record) throws JsonProcessingException {
        log.error("[DLQ] Failed message received: topic={}, partition={}, offset={}, key={}, value={}",
            record.topic(), record.partition(), record.offset(), record.key(), record.value());

        JsonNode root = objectMapper.readTree(record.value());
        JsonNode payload = root.get("payload");
        PaymentCreateMessage message = objectMapper.treeToValue(payload,
            PaymentCreateMessage.class);

        log.warn("[payment_success] Received :{}", message);
    }

    @KafkaListener(topics = "payment_success.DLQ", groupId = "ticket-dlq-group")
    public void consumePaymentSuccessDLQ(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.error("[DLQ] Failed message received: topic={}, partition={}, offset={}, key={}, value={}",
            record.topic(), record.partition(), record.offset(), record.key(), record.value());

        JsonNode root = objectMapper.readTree(record.value());
        JsonNode payload = root.get("payload");

        PaymentSuccessMessage message = objectMapper.treeToValue(payload,
            PaymentSuccessMessage.class);

        log.warn("[payment_success] Received :{}", message);
    }

    @KafkaListener(topics = "payment_fail.DLQ", groupId = "ticket-dlq-group")
    public void consumePaymentFailDLQ(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.error("[DLQ] Failed message received: topic={}, partition={}, offset={}, key={}, value={}",
            record.topic(), record.partition(), record.offset(), record.key(), record.value());

        JsonNode root = objectMapper.readTree(record.value());
        JsonNode payload = root.get("payload");

        PaymentFailMessage message = objectMapper.treeToValue(payload,
            PaymentFailMessage.class);

        log.warn("[payment_success] Received :{}", message);
    }

    @KafkaListener(topics = "payment_fail.DLQ", groupId = "ticket-dlq-group")
    public void consumeSeatExpiredDLQ(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.error("[DLQ] Failed message received: topic={}, partition={}, offset={}, key={}, value={}",
            record.topic(), record.partition(), record.offset(), record.key(), record.value());

        JsonNode root = objectMapper.readTree(record.value());
        JsonNode payload = root.get("payload");

        SeatExpiredMessage message = objectMapper.treeToValue(payload,
            SeatExpiredMessage.class);

        log.warn("[payment_success] Received :{}", message);
    }

}