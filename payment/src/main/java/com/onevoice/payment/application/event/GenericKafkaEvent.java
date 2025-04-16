package com.onevoice.payment.application.event;

public record GenericKafkaEvent<T>(
    String topic,
    T payload
) implements KafkaEvent {

}
