package com.onevoice.seat.application.event;

public record GenericKafkaEvent<T>(
    String topic, T payload
)implements KafkaEvent {
}
