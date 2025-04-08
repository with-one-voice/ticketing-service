package com.onevoice.seat.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Money {
    @Column(nullable = false)
    private int value;

    public Money(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
        this.value = value;
    }
}
