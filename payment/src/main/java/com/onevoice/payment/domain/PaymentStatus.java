package com.onevoice.payment.domain;

public enum PaymentStatus {

    PG_PENDING,
    PG_READY,
    PG_APPROVE,
    PG_CANCEL,
    PG_FAIL,
    ACCOUNT_PENDING,
    ACCOUNT_CONFIRMED,
    CANCEL_REQUEST,
    CANCELED,
    REFUND_REQUEST,
    REFUNDED,
    COMPLETE,
    FAILED,
}