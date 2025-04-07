package com.onevoice.payment.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Refund 애그리거트, 엔티티 클래스
 */
@Entity
@Table(name = "p_refund")
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
}
