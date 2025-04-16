package com.onevoice.show.application.event;

public record GenericKafkaEvent<T>(
    String topic, T payload
) implements KafkaEvent {

}
