package com.onevoice.common.enumtype;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum KafkaTopicType {

    PAYMENT_SUCCESS("payment_success"),
    TICKET_CONFIRM("ticket_confirm"),
    SEAT_CONFIRM("seat_confirm"),
    TICKET_CONFIRM_FAIL("ticket_confirm_fail"),
    SEAT_CONFIRM_FAIL("seat_confirm_fail");

    private final String topic;

    public String getTopic() {
        return topic;
    }
}
