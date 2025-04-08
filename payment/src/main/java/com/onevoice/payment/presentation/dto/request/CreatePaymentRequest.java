package com.onevoice.payment.presentation.dto.request;

import com.onevoice.payment.application.dto.command.CreatePaymentCommand;
import com.onevoice.payment.domain.PaymentMethod;

import java.util.UUID;

public record CreatePaymentRequest(
        UUID ticketId,
        Integer paymentAmount,
        PaymentMethod paymentMethod
) {

    public CreatePaymentCommand toCommand(UUID userId) {
        return CreatePaymentCommand.of(
                ticketId,
                userId,
                paymentAmount,
                paymentMethod
        );
    }
}
