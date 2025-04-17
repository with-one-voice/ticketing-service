package com.onevoice.common.enumtype;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum KafkaTopicType {

    PAYMENT_SUCCESS("payment_success"),
    PAYMENT_FAIL("payment_fail"),

    TICKET_CONFIRM("ticket_confirm"),
    TICKET_STATUS("ticket_status"),
    TICKET_CANCEL("ticket_cancel"),

    SEAT_CONFIRM("seat_confirm"),
    SEAT_CONFIRM_FAIL("seat_confirm_fail"),
    SEAT_CREATE_REQUEST("seat_create_request"),
    SEAT_CREATE_SUCCESS("seat_create_success"),
    SEAT_CREATE_FAIL("seat_create_fail");

    private final String topic;

    public String getTopic() {
        return topic;
    }
}
