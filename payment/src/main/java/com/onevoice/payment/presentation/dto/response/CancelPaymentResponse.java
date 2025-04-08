package com.onevoice.payment.presentation.dto.response;

import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.domain.PaymentStatus;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record CancelPaymentResponse(
        UUID cancellationId,
        UUID paymentId,
        PaymentStatus paymentStatus
) {

    public static CancelPaymentResponse from(FindPaymentQuery query) {
        // TODO: Cancellation Id 를 받아와야 한다.
        return CancelPaymentResponse.builder()
//                .cancellationId(query.)
                .paymentId(query.paymentId())
                .paymentStatus(query.paymentStatus())
                .build();
    }
}
