package com.onevoice.show.infrastructure.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.show.application.event.KafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j(topic = "KafkaTransactionalEventPublisher")
@Component
@RequiredArgsConstructor
public class KafkaTransactionalEventPublisher {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleKafkaEvent(KafkaEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(event.topic(), json);
            log.info("Kafka 토픽 [{}] : {}", event.topic(), json);
        } catch (JsonProcessingException e) {
            log.error("Kafka 직렬화 실패");
            throw new RuntimeException(e);
        }
    }

}
