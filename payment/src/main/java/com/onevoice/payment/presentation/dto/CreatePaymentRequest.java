package com.onevoice.payment.presentation.dto;

import com.onevoice.payment.application.dto.CreatePaymentCommand;
import com.onevoice.payment.domain.MethodType;

import java.util.UUID;

public record CreatePaymentRequest(
        UUID ticketId,
        Long amount,
        MethodType methodType
) {
    public CreatePaymentCommand toCommand() {
        return CreatePaymentCommand.builder()
                .ticketId(ticketId)
                .amount(amount)
                .methodType(methodType)
                .build();
    }
}
