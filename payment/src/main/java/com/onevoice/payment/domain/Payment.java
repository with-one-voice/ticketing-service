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
    private MethodType methodType;

    // 결제 상태
    @Enumerated(EnumType.STRING)
    private Status status;

    // 결제 금액
    private Long amount;

    // 결제 취소
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cancellation cancellation;

    // 환불
    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Refund refund;

    public Payment(UUID ticketId, UUID userId, MethodType methodType, Long amount) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.methodType = methodType;
        this.status = Status.PENDING;
        this.amount = amount;
    }

    // 결제 정보 생성
    public static Payment createPayment(UUID ticketId, UUID userId, MethodType methodType, Long amount) {
        return new Payment(ticketId, userId, methodType, amount);
    }
}
