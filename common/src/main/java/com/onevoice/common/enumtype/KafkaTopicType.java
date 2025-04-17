package com.onevoice.common.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopicType {

    PAYMENT_CREATE("payment_create"),
    PAYMENT_SUCCESS("payment_success"),
    TICKET_CONFIRM("ticket_confirm"),
    SEAT_CONFIRM("seat_confirm"),
    PAYMENT_FAIL("payment_fail"),
    TICKET_CONFIRM_FAIL("ticket_confirm_fail"),
    SEAT_CONFIRM_FAIL("seat_confirm_fail"),
    SEAT_CREATE_REQUEST("seat_create_request"),
    SEAT_CREATE_SUCCESS("seat_create_success"),
    SEAT_CREATE_FAIL("seat_create_fail");

    private final String topic;
}