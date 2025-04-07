package com.onevoice.payment.application.dto;

import com.onevoice.payment.domain.MethodType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreatePaymentCommand(
        UUID ticketId,
        Long amount,
        MethodType methodType
) {
}
