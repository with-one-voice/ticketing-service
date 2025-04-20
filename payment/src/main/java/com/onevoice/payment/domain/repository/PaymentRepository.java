package com.onevoice.payment.domain.repository;

import com.onevoice.payment.domain.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepository {

    Payment save(Payment payment);

    Page<Payment> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Payment> findByPaymentIdAndUserId(UUID paymentId, UUID userId);

    Optional<Payment> findByPaymentId(UUID paymentId);

    Page<Payment> retrieve(UUID userId, String keyword, Pageable pageable);
}
