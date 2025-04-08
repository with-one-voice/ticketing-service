package com.onevoice.payment.application.dto.query;

import com.onevoice.payment.domain.PaymentMethod;
import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.PaymentStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record FindPaymentQuery(
        UUID paymentId,
        UUID ticketId,
        UUID userId,
        String paymentKey,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        long amount,
        LocalDateTime createdAt
) {

    public static FindPaymentQuery from(Payment payment) {
        return FindPaymentQuery.builder()
                .paymentId(payment.getPaymentId())
                .ticketId(payment.getTicketId())
                .userId(payment.getUserId())
                .paymentKey(payment.getPaymentKey())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .amount(payment.getAmount())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
