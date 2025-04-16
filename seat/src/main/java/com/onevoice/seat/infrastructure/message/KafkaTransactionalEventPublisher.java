package com.onevoice.seat.infrastructure.message;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.seat.application.event.KafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTransactionalEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleKafkaMessage(KafkaEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(event.topic(), json);
            log.info("[Kafka 발행됨] topic: {}, payload: {}", event.topic(), json);
        } catch (JsonProcessingException e) {
            log.error("Kafka 직렬화 실패", e);
            throw new RuntimeException(e);
        }
    }


}
