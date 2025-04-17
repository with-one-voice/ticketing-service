package com.onevoice.ticket.domain;

import com.onevoice.common.entity.BaseEntity;
import com.onevoice.common.enumtype.TicketStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;


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

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketSeat> ticketSeatList = new ArrayList<>();

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Version
    @Column(name = "version")
    private Long version;

    public Ticket(UUID userId,String userName, UUID sessionId,String showName) {
        this.userId = userId;
        this.userName = userName;
        this.sessionId = sessionId;
        this.showName = showName;
        this.reservedAt = LocalDateTime.now();
        this.status = TicketStatus.WAITING_PAYMENT;

        if(ticketSeatList ==null){
            ticketSeatList = new ArrayList<>();
        }
    }

    public void updateTicketStatus(TicketStatus status) {
        this.status = status;
    }

}
