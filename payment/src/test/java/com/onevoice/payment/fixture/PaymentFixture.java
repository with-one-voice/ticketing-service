package com.onevoice.payment.fixture;

import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.PaymentMethod;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.test.util.ReflectionTestUtils;

public class PaymentFixture {

    private static final AtomicInteger counter = new AtomicInteger(1);

    public static UUID nextUUID() {
        long count = counter.getAndIncrement();
        return UUID.nameUUIDFromBytes(String.valueOf(count)
            .getBytes(StandardCharsets.UTF_8));
    }

    public static Payment createPayment() {
        Payment payment = Payment.create(
            UUID.randomUUID(),
            UUID.randomUUID(),
            PaymentMethod.CARD,
            10000
        );

        ReflectionTestUtils.setField(
            payment,
            "paymentId",
            nextUUID()
        );
        return payment;
    }

    public static Payment createPayment(Payment payment) {
        ReflectionTestUtils.setField(
            payment,
            "paymentId",
            nextUUID()
        );
        return payment;
    }
}
