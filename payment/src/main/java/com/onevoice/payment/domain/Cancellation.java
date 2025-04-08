package com.onevoice.payment.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Cancellation 애그리거트, 엔티티 클래스
 */
@Entity
@Table(name = "p_cancellation")
@NoArgsConstructor
public class Cancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cancellation_id;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    // 취소 사유
    private String reason;

    // 취소 요청 시각
    private LocalDateTime requestedAt;

    private Cancellation(String reason) {
        this.reason = reason;
        this.requestedAt = LocalDateTime.now();
    }

    // 접근제어자 package-private 사용
    static Cancellation create(String reason) {
        return new Cancellation(reason);
    }

    void assignPayment(Payment payment) {
        this.payment = payment;
    }
}
