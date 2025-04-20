package com.onevoice.payment.application.dto.command;

import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.PaymentMethod;
import java.util.UUID;

public record CreatePaymentCommand(
    UUID ticketId,
    UUID userId,
    PaymentMethod paymentMethod,
    Integer paymentAmount
) {

    public static CreatePaymentCommand of(
        UUID ticketId,
        UUID userId,
        PaymentMethod paymentMethod,
        Integer paymentAmount
    ) {
        return new CreatePaymentCommand(
            ticketId,
            userId,
            paymentMethod,
            paymentAmount
        );
    }

    public Payment to() {
        return Payment.create(
            ticketId,
            userId,
            paymentMethod,
            paymentAmount
        );
    }
}
