package com.onevoice.payment.application.event;

import java.util.UUID;

public record PaymentTimeoutEvent(
    UUID paymentId
) {

}
