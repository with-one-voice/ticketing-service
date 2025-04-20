package com.onevoice.payment.presentation.dto.request;

import com.onevoice.payment.application.dto.command.CreatePaymentCommand;
import com.onevoice.payment.domain.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record CreatePaymentRequest(
    // UUID, Long, Integer 같은 타입에는 @NotNull, @NotBlank 는 String 만 사용 가능
    @NotNull(message = "티켓 정보가 필요합니다.")
    UUID ticketId,

    @NotNull(message = "가격 정보가 필요합니다.")
    @Positive(message = "가격은 양수입니다.")
    Integer paymentAmount,

    @NotNull(message = "결제 방법을 올바르게 지정해 주세요. CARD | ACCOUNT")
    PaymentMethod paymentMethod
) {

    public CreatePaymentCommand to(UUID userId) {
        return CreatePaymentCommand.of(
            ticketId,
            userId,
            paymentMethod,
            paymentAmount
        );
    }
}
