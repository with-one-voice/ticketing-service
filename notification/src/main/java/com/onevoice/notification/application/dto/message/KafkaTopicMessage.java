package com.onevoice.notification.application.dto.message;

import java.util.UUID;

public record KafkaTopicMessage(
    String topic,
    Payload payload
) {

    public record Payload(
        UUID userId
    ) {

    }
}
