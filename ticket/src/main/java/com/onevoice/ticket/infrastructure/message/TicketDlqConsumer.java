package com.onevoice.ticket.infrastructure.message;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TicketDlqConsumer {

    @KafkaListener(topics = "payment_success.DLQ", groupId = "ticket-dlq-group")
    public void consumeDlq(ConsumerRecord<String, String> record) {
        log.error("[DLQ] Failed message received: topic={}, partition={}, offset={}, key={}, value={}",
            record.topic(), record.partition(), record.offset(), record.key(), record.value());

        // 필요 시 file 또는 DB 저장도??
    }
}