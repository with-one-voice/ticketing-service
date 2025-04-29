package com.onevoice.payment.infrastructure.jpa;

import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.PaymentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {

    Page<Payment> findAllByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);

    Optional<Payment> findByPaymentIdAndUserIdAndDeletedAtIsNull(UUID paymentId, UUID userId);

    Optional<Payment> findByPaymentIdAndDeletedAtIsNull(UUID paymentId);

    List<Payment> findByPaymentIdAndPaymentStatusEqualsAndDeletedAtIsNull(UUID paymentId,
        PaymentStatus paymentStatus);
}
