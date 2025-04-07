package com.onevoice.payment.domain.repository;

import com.onevoice.payment.domain.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);
}
