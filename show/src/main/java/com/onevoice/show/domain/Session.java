package com.onevoice.show.domain;

import com.onevoice.common.entity.BaseEntity;
import com.onevoice.show.presentation.dto.request.UpdateSessionRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_sessions")
@SQLRestriction("deleted_at IS NULL")
public class Session extends BaseEntity {

    @Id
    @Column(name = "session_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(nullable = false, name = "session_date")
    private LocalDate sessionDate;

    @Column(nullable = false, name = "start_time")
    private LocalTime startTime;

    @Column(nullable = false, name = "end_time")
    private LocalTime endTime;

    @Column(nullable = false, name = "seat_count")
    private Integer seatCount;

    @Column(nullable = false, name = "seat_price")
    private Long seatPrice;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void update(UpdateSessionRequestDto requestDto) {
        this.sessionDate = requestDto.sessionDate();
        this.startTime = requestDto.startTime();
        this.endTime = requestDto.endTime();
        this.seatPrice = requestDto.seatPrice();
    }

    public void updateStatusBefore() {
        this.status = Status.BEFORE;
    }

    public void updateStatusCancelled() {
        this.status = Status.CANCELLED;
    }

    public void updateStatusByTime(LocalDateTime now) {
        if (this.status == Status.CANCELLED || this.status == Status.SOLD_OUT) {
            return;
        }

        LocalDateTime start = this.getShow().getTicketingStartTime();
        LocalDateTime end = this.getShow().getTicketingEndTime();

        if (now.isBefore(start)) {
            this.status = Status.BEFORE;
        } else if (now.isAfter(end)) {
            this.status = Status.CLOSED;
        } else {
            this.status = Status.OPEN;
        }
    }
}
