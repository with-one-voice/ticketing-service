package com.onevoice.payment.domain;

import com.onevoice.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Payment 애그리거트 루트, 엔티티 클래스
 */
@Entity
@NoArgsConstructor
@Table(name = "p_payments")
@Getter
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
    private int paymentAmount;

    // 결제 취소
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cancellation cancellation;

    // 환불
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Refund refund;

    private Payment(
        UUID ticketId,
        UUID userId,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        Integer paymentAmount
    ) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentAmount = paymentAmount;
    }

    // 결제 정보 생성
    public static Payment createPayment(
        UUID ticketId,
        UUID userId,
        PaymentMethod paymentMethod,
        Integer paymentAmount
    ) {
        // TODO: method 에 따라 status 를 다르게 해야 한다. 일단은 바로 완료처리
        // card: 즉시 완료(pg 연동 후에 변경)
        // account: 대기 (이건 스케쥴러로 일정시간 마다 업데이트 되도록 처리)
        PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
//        if (PaymentMethod.CARD == paymentMethod) {
//            paymentStatus = PaymentStatus.COMPLETE;
//        }

        return new Payment(ticketId, userId, paymentMethod, paymentStatus, paymentAmount);
    }

    // 취소 처리 메서드: 애그리거트 루트인 Payment 에서 Cancellation 을 조작한다.
    public void cancel(String reason) {
        // 취소 생성은 취소 애그리거트에
        this.cancellation = Cancellation.create(reason);

        // 연관관계 편의 메소드
        cancellation.assignPayment(this);

        // TODO: 취소 가능 여부를 어디서 정할지...
        // 상태값 변경
        this.paymentStatus = PaymentStatus.CANCEL_REQUEST;
    }

    // 환불 처리 메서드
    public void refund(String reason) {
        // 환불 생성은 환불 애그리거트에
        this.refund = Refund.create(reason);

        // 연관관계 편의 메소드
        refund.assignPayment(this);

        // 상태값 변경
        this.paymentStatus = PaymentStatus.REFUND_REQUEST;
    }
}

