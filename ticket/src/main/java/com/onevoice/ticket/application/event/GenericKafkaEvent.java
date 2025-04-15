package com.onevoice.ticket.application.event;

public record GenericKafkaEvent<T>(
    String topic, T payload
)implements KafkaEvent {
}
