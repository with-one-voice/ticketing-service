package com.onevoice.seat.infrastructure.message;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SeatDlqConsumer {
    @KafkaListener(topics = "seat_create_request.DLQ", groupId = "seat-dlq-group")
    public void consumeDlq(ConsumerRecord<String, String> record) {
        log.error("[DLQ] 좌석 생성 요청 처리 실패 메시지 수신: topic={}, partition={}, offset={}, key={}, value={}",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());


    }
    @KafkaListener(topics = "ticket_confirm.DLQ", groupId = "seat-dlq-group")
    public void consumeTicketConfirmDlq(ConsumerRecord<String, String> record) {
        log.error("[DLQ] ticket_confirm 처리 실패: {}", record.value());
    }

    @KafkaListener(topics = "ticket_cancel.DLQ", groupId = "seat-dlq-group")
    public void consumeTicketCancelDlq(ConsumerRecord<String, String> record) {
        log.error("[DLQ] ticket_cancel 처리 실패: {}", record.value());
    }
}
