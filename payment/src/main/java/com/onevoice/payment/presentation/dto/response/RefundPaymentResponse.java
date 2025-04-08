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
        // TODO: Refund 의 값을 가져와야 한다.
        return new RefundPaymentResponse(
                UUID.randomUUID(), // refundId
                10000L, // refundAmount
                RefundStatus.PENDING // refundStatus
        );
    }
}
