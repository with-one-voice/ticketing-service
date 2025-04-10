package com.onevoice.payment.presentation.dto.response;

import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.domain.RefundStatus;
import java.util.UUID;

public record RefundPaymentResponse(
    UUID refundId,
    long refundAmount,
    RefundStatus refundStatus
) {

    public static RefundPaymentResponse from(FindPaymentQuery query) {
        return new RefundPaymentResponse(
            query.refundQuery().refundId(),
            query.refundQuery().refundAmount(),
            query.refundQuery().refundStatus()
        );
    }
}
