package com.onevoice.payment.presentation.dto.response;

import com.onevoice.payment.domain.Status;

import java.util.UUID;

public record CancelPaymentResponse(
        UUID cancellationId,
        UUID paymentId,
        Status status
) {
//    public static CancelPaymentResponse from( payment) {
//
//    }
}
