package com.onevoice.payment.fixture;

import com.onevoice.payment.application.dto.query.FindPaymentQuery;
import com.onevoice.payment.domain.PaymentMethod;
import com.onevoice.payment.domain.PaymentStatus;
import com.onevoice.payment.presentation.dto.request.CreatePaymentRequest;
import java.time.LocalDateTime;
import java.util.UUID;

public class RequestFixture {

    public static CreatePaymentRequest validRequest() {
        return new CreatePaymentRequest(
            UUID.randomUUID(),
            10000,
            PaymentMethod.CARD
        );
    }

    public static CreatePaymentRequest invalidRequest() {
        return new CreatePaymentRequest(
            UUID.randomUUID(),
            10000,
            null
        );
    }

    public static FindPaymentQuery createFindQuery(UUID paymentId) {
        return new FindPaymentQuery(
            paymentId,
            UUID.randomUUID(),
            UUID.randomUUID(),
            "pgKey",
            PaymentMethod.CARD,
            PaymentStatus.COMPLETE,
            10000,
            LocalDateTime.now(),
            null, null
        );
    }
}
