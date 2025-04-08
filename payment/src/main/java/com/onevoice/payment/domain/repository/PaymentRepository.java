package com.onevoice.payment.domain.repository;

import com.onevoice.payment.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Page<Payment> findAll(Pageable pageable);

    Optional<Payment> findById(UUID paymentId);
}
