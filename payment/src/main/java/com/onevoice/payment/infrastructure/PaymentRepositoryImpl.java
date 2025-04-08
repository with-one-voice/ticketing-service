package com.onevoice.payment.infrastructure;

import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.QPayment;
import com.onevoice.payment.domain.repository.PaymentRepository;
import com.onevoice.payment.infrastructure.jpa.PaymentJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Payment save(Payment payment) {
        return jpaRepository.save(payment);
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Payment> findById(UUID paymentId) {
        QPayment payment = QPayment.payment;
        return Optional.ofNullable(queryFactory
                .selectFrom(payment)
                .where(payment.paymentId.eq(paymentId))
                .fetchFirst());
    }
}
