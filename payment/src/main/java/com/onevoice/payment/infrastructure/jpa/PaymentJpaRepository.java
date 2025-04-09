package com.onevoice.payment.infrastructure.jpa;

import com.onevoice.payment.domain.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {

    Page<Payment> findAllByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);

    Optional<Payment> findByPaymentIdAndUserIdAndDeletedAtIsNull(UUID paymentId, UUID userId);
}
