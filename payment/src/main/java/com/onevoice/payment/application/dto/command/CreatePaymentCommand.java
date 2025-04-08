package com.onevoice.payment.application.dto.command;

import com.onevoice.payment.domain.PaymentMethod;

import java.util.UUID;

public record CreatePaymentCommand(
        UUID ticketId,
        UUID userId,
        Integer paymentAmount,
        PaymentMethod paymentMethod
) {

    public static CreatePaymentCommand of(
            UUID ticketId,
            UUID userId,
            Integer paymentAmount,
            PaymentMethod paymentMethod
    ) {
        return new CreatePaymentCommand(
                ticketId,
                userId,
                paymentAmount,
                paymentMethod
        );
    }
}
