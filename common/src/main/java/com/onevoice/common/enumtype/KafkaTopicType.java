package com.onevoice.common.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KafkaTopicType {

    PAYMENT_CREATE("payment_create"),
    PAYMENT_SUCCESS("payment_success"),
    PAYMENT_FAIL("payment_fail"),

    TICKET_CONFIRM("ticket_confirm"),
    TICKET_STATUS("ticket_status"),
    TICKET_CANCEL("ticket_cancel"),

    SEAT_CONFIRM("seat_confirm"),
    SEAT_CONFIRM_FAIL("seat_confirm_fail"),
    SEAT_CANCEL("seat_cancel"),
    SEAT_CREATE_REQUEST("seat_create_request"),
    SEAT_EXPIRED("seat_expired"),
    SEAT_CREATE_SUCCESS("seat_create_success"),
    SEAT_CREATE_FAIL("seat_create_fail");

    private final String topic;
}