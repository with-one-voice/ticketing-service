package com.onevoice.payment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Refund 애그리거트, 엔티티 클래스
 */
@Entity
@Table(name = "p_refund")
@NoArgsConstructor
@Getter
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID refundId;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    // 환불 금액
    private int refundAmount;

    // 환불 사유
    private String reason;

    // 환불 상태
    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;

    // 환불 요청 시각
    private LocalDateTime requestedAt;

    // 환불 완료 시각
    private LocalDateTime completedAt;

    private Refund(String reason) {
        this.reason = reason;
        this.refundStatus = RefundStatus.PENDING;
        this.requestedAt = LocalDateTime.now();
        // TODO: 환불 금액 계산하는 로직
    }

    // 접근제어자 package-private 사용
    static Refund create(String reason) {
        return new Refund(reason);
    }

    void assignPayment(Payment payment) {
        this.payment = payment;
    }
}
