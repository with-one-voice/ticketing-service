package com.onevoice.ticket.domain;

import com.onevoice.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Getter
@NoArgsConstructor
@Table(name ="p_tickets")
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name ="user_name",nullable = false)
    private String userName;

    @Column(name = "show_id", nullable = false)
    private UUID showId;

    @Column(name = "show_name",nullable = false)
    private String showName;

    @Column(name = "seat_id", nullable = false)
    private UUID seatId;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    public Ticket(UUID userId,String userName, UUID showId,String showName, UUID seatId) {
        this.userId = userId;
        this.userName = userName;
        this.showId = showId;
        this.showName = showName;
        this.seatId = seatId;
        this.reservedAt = LocalDateTime.now();
    }

    public void updateTicketStatus(TicketStatus status) {
        this.status = status;
    }

}
