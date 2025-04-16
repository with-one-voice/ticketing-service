package com.onevoice.show.infrastructure.redis;

import com.onevoice.show.presentation.dto.response.SessionDetailResponseDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SessionCache {

    private UUID sessionId;
    private UUID showId;
    private String showName;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer seatCount;
    private Long seatPrice;

    public static SessionDetailResponseDto toDto(SessionCache cache) {
        return new SessionDetailResponseDto(
            cache.getSessionId(),
            cache.getShowId(),
            cache.getShowName(),
            cache.getSessionDate(),
            cache.getStartTime(),
            cache.getEndTime(),
            cache.getSeatCount(),
            cache.getSeatPrice()
        );
    }
}
