package com.onevoice.payment.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefundPaymentRequest(
    @NotBlank(message = "환불 사유를 적어주세요.")
    String reason
) {

}
