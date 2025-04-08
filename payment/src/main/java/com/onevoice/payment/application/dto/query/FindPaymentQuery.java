package com.onevoice.payment.application.dto.query;

import com.onevoice.payment.domain.MethodType;
import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.Status;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record FindPaymentQuery(
        UUID paymentId,
        UUID ticketId,
        UUID userId,
        String paymentKey,
        MethodType methodType,
        Status status,
        long amount,
        LocalDateTime createdAt
) {

    public static FindPaymentQuery from(Payment payment) {
        return FindPaymentQuery.builder()
                .paymentId(payment.getPaymentId())
                .ticketId(payment.getTicketId())
                .userId(payment.getUserId())
                .paymentKey(payment.getPaymentKey())
                .methodType(payment.getMethodType())
                .status(payment.getStatus())
                .amount(payment.getAmount())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
