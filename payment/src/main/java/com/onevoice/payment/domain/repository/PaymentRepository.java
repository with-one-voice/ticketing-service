package com.onevoice.payment.domain.repository;

import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.PaymentStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepository {

    Payment save(Payment payment);

    Page<Payment> findAllByUserId(UUID userId, Pageable pageable);

    Optional<Payment> findByIdAndUserId(UUID paymentId, UUID userId);

    Optional<Payment> findById(UUID paymentId);

    List<Payment> findByUserIdAndPaymentStatus(UUID userId, PaymentStatus status);

    Page<Payment> retrieve(UUID userId, String keyword, Pageable pageable);
}
