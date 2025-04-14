package com.onevoice.ticket.domain;

import com.onevoice.common.entity.BaseEntity;
import com.onevoice.common.enumtype.TicketStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Getter
@NoArgsConstructor
@Table(
    name ="p_tickets",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_session_seat", columnNames = {"session_id", "seat_id"})
    })
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name ="user_name",nullable = false)
    private String userName;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Column(name = "show_name",nullable = false)
    private String showName;

    @Column(name = "seat_id", nullable = false)
    private UUID seatId;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Version
    @Column(name = "version")
    private Long version;

    public Ticket(UUID userId,String userName, UUID sessionId,String showName, UUID seatId) {
        this.userId = userId;
        this.userName = userName;
        this.sessionId = sessionId;
        this.showName = showName;
        this.seatId = seatId;
        this.reservedAt = LocalDateTime.now();
        this.status = TicketStatus.WAITING_PAYMENT;
    }

    public void updateTicketStatus(TicketStatus status) {
        this.status = status;
    }

}
