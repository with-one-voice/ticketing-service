package com.onevoice.payment.application.dto;

import java.util.UUID;

public record FindPaymentQuery(
        UUID paymentId
) {
}
