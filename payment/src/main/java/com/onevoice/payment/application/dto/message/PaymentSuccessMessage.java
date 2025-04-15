package com.onevoice.payment.application.dto.message;

import java.util.UUID;

public record PaymentSuccessMessage(
    UUID ticketId
) {

}
