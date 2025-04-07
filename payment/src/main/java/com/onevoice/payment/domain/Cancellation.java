package com.onevoice.payment.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Cancellation 애그리거트, 엔티티 클래스
 */
@Entity
@Table(name = "p_cancellation")
public class Cancellation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cancellation_id;

    @OneToOne
    private Payment payment;

    // 취소 사유
    private String reason;

    // 취소 요청 시각
    private LocalDateTime requestedAt;
}
