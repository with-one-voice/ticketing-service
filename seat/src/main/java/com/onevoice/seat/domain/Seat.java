package com.onevoice.seat.domain;

import com.onevoice.common.entity.BaseEntity;
import com.onevoice.common.enumtype.SeatStatus;
import com.onevoice.seat.domain.vo.Money;
import com.onevoice.seat.domain.vo.SeatCode;
import com.onevoice.seat.domain.vo.SessionId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_seats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seat_id")
    private UUID seatId;

    private UUID userId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "seat_code"))
    private SeatCode seatCode;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "session_id"))
    private SessionId sessionId;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Money price;


    public Seat(SeatCode seatCode, SessionId sessionId, SeatStatus status, Money price) {
        this.seatCode = seatCode;
        this.sessionId = sessionId;
        this.status = status;
        this.price = price;
    }
    /*
    * 상태만 바꾼 복제본 만들어줌
    * */
    public Seat withStatus(SeatStatus newStatus) {
        Seat clone = new Seat(this.seatCode, this.sessionId, newStatus, this.price);
        clone.seatId = this.seatId;
        clone.userId = this.userId;
        return clone;
    }
    /*
    * 좌석 상태 변경
    * */
    public void changeStatus(SeatStatus status) {
        this.status = status;
    }

    public void assignUser(UUID userId) {
        this.userId = userId;
    }
    public void clearUserId() {
        this.userId = null;
    }
}
