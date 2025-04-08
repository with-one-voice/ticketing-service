package com.onevoice.payment.presentation.dto.response;

import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.domain.PaymentStatus;

import java.util.UUID;

public record CancelPaymentResponse(
        UUID cancellationId,
        UUID paymentId,
        PaymentStatus paymentStatus
) {

    public static CancelPaymentResponse from(FindPaymentQuery query) {
        // TODO: Cancellation Id 를 받아와야 한다.
        return new CancelPaymentResponse(
                UUID.randomUUID(), // cancellationId
                query.paymentId(),
                query.paymentStatus()
        );
    }
}
