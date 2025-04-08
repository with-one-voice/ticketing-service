package com.onevoice.seat.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class SessionId {
    @Column(nullable = false)
    private UUID value;

    public SessionId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("세션 ID는 비어 있을 수 없습니다.");
        }
        this.value = value;
    }
}