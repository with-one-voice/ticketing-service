package com.onevoice.payment.domain;

import jakarta.persistence.Entity;
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
 * Cancellation 애그리거트, 엔티티 클래스
 */
@Entity
@Table(name = "p_cancellation")
@NoArgsConstructor
@Getter
public class Cancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cancellationId;

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
