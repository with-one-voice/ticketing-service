package com.onevoice.payment.application.dto.command;

import com.onevoice.payment.domain.MethodType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreatePaymentCommand(
        UUID ticketId,
        UUID userId,
        Long amount,
        MethodType methodType
) {
}
