package com.onevoice.seat.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class SeatCode {
    @Column(name = "seat_code", nullable = false)
    private String value;

    public SeatCode(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("좌석 코드는 비어 있을 수 없습니다.");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
