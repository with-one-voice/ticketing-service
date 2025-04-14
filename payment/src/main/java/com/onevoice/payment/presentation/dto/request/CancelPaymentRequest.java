package com.onevoice.payment.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CancelPaymentRequest(
    @NotBlank(message = "취소 사유를 적어주세요.")
    String reason
) {

}
