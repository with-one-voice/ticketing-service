package com.onevoice.payment.domain;

import com.onevoice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Payment 애그리거트 루트, 엔티티 클래스
 */
@Getter
@Entity
@NoArgsConstructor
@Table(name = "p_payments")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    private UUID ticketId;

    private UUID userId;

    // PG 사의 결제 정보
    private String paymentKey;

    // 결제 방법
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    // 결제 상태
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    // 결제 금액
    private Long amount;

    // 결제 취소
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cancellation cancellation;

    // 환불
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Refund refund;

    public Payment(
            UUID ticketId,
            UUID userId,
            PaymentMethod paymentMethod,
            Long amount
    ) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PENDING;
        this.amount = amount;
    }

    // 결제 정보 생성
    public static Payment createPayment(
            UUID ticketId,
            UUID userId,
            PaymentMethod paymentMethod,
            Long amount
    ) {
        return new Payment(ticketId, userId, paymentMethod, amount);
    }

    // 취소 처리 메서드: 애그리거트 루트인 Payment 에서 Cancellation 을 조작한다.
    public void cancel(String reason) {
        // 취소 생성은 취소 애그리거트에
        this.cancellation = Cancellation.create(reason);

        // 연관관계 편의 메소드
        cancellation.assignPayment(this);
    }

    // 환불 처리 메서드
    public void refund(String reason) {
        // 환불 생성은 환불 애그리거트에
        this.refund = Refund.create(reason);

        // 연관관계 편의 메소드
        refund.assignPayment(this);
    }
}
