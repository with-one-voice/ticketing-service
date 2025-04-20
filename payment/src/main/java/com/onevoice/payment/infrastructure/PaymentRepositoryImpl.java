package com.onevoice.payment.infrastructure;

import static com.onevoice.payment.domain.QPayment.payment;

import com.onevoice.payment.domain.Payment;
import com.onevoice.payment.domain.PaymentStatus;
import com.onevoice.payment.domain.repository.PaymentRepository;
import com.onevoice.payment.exception.PaymentNotStatusException;
import com.onevoice.payment.infrastructure.jpa.PaymentJpaRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
    public Page<Payment> findAllByUserId(UUID userId, Pageable pageable) {
        // deletedAt is null
        return jpaRepository.findAllByUserIdAndDeletedAtIsNull(userId, pageable);
    }

    @Override
    public Optional<Payment> findByPaymentIdAndUserId(UUID paymentId, UUID userId) {
        return jpaRepository.findByPaymentIdAndUserIdAndDeletedAtIsNull(paymentId, userId);
    }

    @Override
    public Optional<Payment> findByPaymentId(UUID paymentId) {
        return jpaRepository.findByPaymentIdAndDeletedAtIsNull(paymentId);
    }

    @Override
    public Page<Payment> retrieve(UUID userId, String keyword, Pageable pageable) {
        List<Payment> paymentList = getPaymentList(userId, keyword, pageable);
        return new PageImpl<>(paymentList, pageable, paymentList.size());
    }

    /**
     * 페이징 + 검색 메서드
     */
    private List<Payment> getPaymentList(UUID userId, String keyword, Pageable pageable) {
        return queryFactory
            .selectFrom(payment)
            .where(getWhereConditions(userId, keyword))
            .orderBy(getAllOrderSpecifiers(pageable).toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    /**
     * 조회 조건
     */
    private BooleanBuilder getWhereConditions(UUID userId, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();

        // soft delete 정책 적용
        builder.and(payment.deletedAt.isNull());

        // keyword 가 있을 경우 검색 조건 추가, 현재 키워드로는 결제 상태 검색
        if (StringUtils.hasText(keyword)) {
            try {
                PaymentStatus status = PaymentStatus.valueOf(keyword);
                builder.and(payment.paymentStatus.eq(status));
            } catch (IllegalArgumentException e) {
                throw new PaymentNotStatusException();
            }
        }

        // userId 가 있을 경우 검색 조건 추가
        if (userId != null) {
            builder.and(payment.userId.eq(userId));
        }

        return builder;
    }

    /**
     * 정렬 조건
     */
    private List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> specifierList = new ArrayList<>();

        for (Sort.Order sortOrder : pageable.getSort()) {
            Order direction = sortOrder.isAscending() ? Order.ASC : Order.DESC;
            switch (sortOrder.getProperty()) {
                case "createdAt" -> specifierList.add(
                    new OrderSpecifier<>(direction, payment.createdAt));
                case "paymentStatus" -> specifierList.add(
                    new OrderSpecifier<>(direction, payment.paymentStatus));
                case "paymentMethod" -> specifierList.add(
                    new OrderSpecifier<>(direction, payment.paymentMethod));
                case "paymentAmount" -> specifierList.add(
                    new OrderSpecifier<>(direction, payment.paymentAmount));
                default -> throw new IllegalStateException("Unexpected value: "
                    + sortOrder.getProperty());
            }
        }
        return specifierList;
    }
}
