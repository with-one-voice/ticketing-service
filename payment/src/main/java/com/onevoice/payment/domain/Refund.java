package com.onevoice.payment.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Refund 애그리거트, 엔티티 클래스
 */
@Entity
@Table(name = "p_refund")
@NoArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID refund_id;

    @OneToOne
    private Payment payment;

    // 환불 금액
    private int amount;

    // 환불 사유
    private String reason;

    // 환불 상태
    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    // 환불 요청 시각
    private LocalDateTime requestedAt;

    // 환불 완료 시각
    private LocalDateTime completedAt;

    private Refund(String reason) {
        this.reason = reason;
        this.status = RefundStatus.PENDING;
        this.requestedAt = LocalDateTime.now();
        // TODO: 환불 금액 계산하는 로직
    }

    public static Refund create(String reason) {
        return new Refund(reason);
    }

    public void assignPayment(Payment payment) {
        this.payment = payment;
    }
}
