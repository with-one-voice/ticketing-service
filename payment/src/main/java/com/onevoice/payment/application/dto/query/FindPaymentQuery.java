package com.onevoice.payment.application.dto.query;

import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.PaymentMethod;
import com.onevoice.payment.domain.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record FindPaymentQuery(
        UUID paymentId,
        UUID ticketId,
        UUID userId,
        String paymentKey,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        long paymentAmount,
        LocalDateTime createdAt
) {

    public static FindPaymentQuery from(Payment payment) {
        return new FindPaymentQuery(
                payment.getPaymentId(),
                payment.getTicketId(),
                payment.getUserId(),
                payment.getPaymentKey(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaymentAmount(),
                payment.getCreatedAt()
        );
    }
}
