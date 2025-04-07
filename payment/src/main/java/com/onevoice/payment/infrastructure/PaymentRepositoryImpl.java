package com.onevoice.payment.infrastructure;

import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.repository.PaymentRepository;
import com.onevoice.payment.infrastructure.jpa.PaymentJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Payment save(Payment payment) {
        return jpaRepository.save(payment);
    }
}
