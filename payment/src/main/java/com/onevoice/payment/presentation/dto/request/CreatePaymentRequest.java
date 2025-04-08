package com.onevoice.payment.presentation.dto.request;

import com.onevoice.payment.application.dto.command.CreatePaymentCommand;
import com.onevoice.payment.domain.MethodType;

import java.util.UUID;

public record CreatePaymentRequest(
        UUID ticketId,
        Long amount,
        MethodType methodType
) {

    public CreatePaymentCommand toCommand(UUID userId) {
        return CreatePaymentCommand.builder()
                .ticketId(ticketId)
                .userId(userId)
                .amount(amount)
                .methodType(methodType)
                .build();
    }
}
