package com.onevoice.notification.infrastructure.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j(topic = "KafkaConsumer")
@Component
public class KafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "notification")
    public void listen(String message) {
        log.info("Received message: {}", message);
    }
}
