package com.onevoice.payment.application.dto.message;

import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.PaymentMethod;
import com.onevoice.payment.domain.PaymentStatus;
import java.util.UUID;

public record PaymentMessage(
    UUID paymentId,
    UUID ticketId,
    UUID userId,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus
) {

    public static PaymentMessage from(Payment payment) {
        return new PaymentMessage(
            payment.getPaymentId(),
            payment.getTicketId(),
            payment.getUserId(),
            payment.getPaymentMethod(),
            payment.getPaymentStatus()
        );
    }
}
