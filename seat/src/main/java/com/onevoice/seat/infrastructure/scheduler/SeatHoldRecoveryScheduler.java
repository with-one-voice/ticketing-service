package com.onevoice.seat.infrastructure.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatHoldRecoveryScheduler {
    private final StringRedisTemplate redisTemplate;

    @Scheduled(fixedDelay = 300_000) // 5분마다
    public void recoverExpiredHolds() {
        log.info("TTL 만료 좌석 복구 스케쥴러 실행");
        Set<String> keys = redisTemplate.keys("seat:*");
        if (keys == null || keys.isEmpty()) return; //아무 키도 존재 안 하면 조기 종료

        for (String key : keys) {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                String seatId = (String) entry.getKey();
                String status = (String) entry.getValue();

                // 상태가 HOLD인 좌석은 TTL 키를 확인
                if ("HOLD".equals(status)) {
                    String ttlKey = "seat-hold:" + key.split(":")[1] + ":" + seatId;

                    Boolean exists = redisTemplate.hasKey(ttlKey);
                    if (Boolean.FALSE.equals(exists)) {
                        // TTL 만료된 경우 → 상태를 AVALIABLE로 복구
                        redisTemplate.opsForHash().put(key, seatId, "AVAILABLE");
                        log.info("TTL 만료 → 좌석 [{}] 상태 복구 완료", seatId);
                    }
                }
            }
        }
    }
}
