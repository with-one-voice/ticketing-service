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
        return new CancelPaymentResponse(
            query.cancellationQuery().cancellationId(),
            query.paymentId(),
            query.paymentStatus()
        );
    }
}
