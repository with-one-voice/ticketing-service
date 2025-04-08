package com.onevoice.payment.presentation.dto.response;

import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.domain.RefundStatus;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record RefundPaymentResponse(
        UUID refundId,
        long amount,
        RefundStatus refundStatus
) {

    public static RefundPaymentResponse from(FindPaymentQuery query) {
        return RefundPaymentResponse.builder()
                // TODO: Refund 의 값을 가져와야 한다.
//                .refundId(query.re)
                .amount(10000L)
                .refundStatus(RefundStatus.PENDING)
                .build();
    }
}
