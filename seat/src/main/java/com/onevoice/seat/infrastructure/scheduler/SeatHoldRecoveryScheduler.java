package com.onevoice.seat.infrastructure.scheduler;

import com.onevoice.seat.infrastructure.message.SeatEventProducer;
import com.onevoice.seat.infrastructure.redis.RedisKeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatHoldRecoveryScheduler {
    private final StringRedisTemplate redisTemplate;
    private final SeatEventProducer seatEventProducer;

    @Scheduled(fixedDelay = 60_000) //1분마다
    public void recoverExpiredHolds() {
        log.info("TTL 만료 좌석 복구 스케쥴러 실행");
        Set<String> keys = redisTemplate.keys("seat:*");
        if (keys == null || keys.isEmpty()) return; //아무 키도 존재 안 하면 조기 종료

        //좌석 상태별로 순회
        for (String redisKey : keys) {
            String[] parts = redisKey.split(":");
            if (parts.length != 2) continue;

            UUID sessionId = UUID.fromString(parts[1]);
            //좌석별 상태 확인
            Map<Object, Object> seatStatusMap = redisTemplate.opsForHash().entries(redisKey);


            //userId → 만료된 seatId 리스트
            //각 사용자마다 만료된 좌석들 모아두는 자료구조
            Map<UUID, List<UUID>> expiredSeatsByUser = new HashMap<>();


            for (Map.Entry<Object, Object> entry : seatStatusMap.entrySet()) {
                String seatIdStr = (String) entry.getKey();
                String status = (String) entry.getValue();
                if (!"HOLD".equals(status)) continue;
                UUID seatId = UUID.fromString(seatIdStr);
                String holdKey = RedisKeyUtil.seatHoldKey(sessionId, seatId);
                String raw = redisTemplate.opsForValue().get(holdKey);

                if (raw == null) continue;

                try {
                    String[] parts2 = raw.split(",", 2);
                    UUID userId = UUID.fromString(parts2[0]);
                    LocalDateTime expireAt = LocalDateTime.parse(parts2[1]);

                    boolean expired = LocalDateTime.now().isAfter(expireAt);
                    log.info("[{}] 복구 체크 - now={}, expireAt={}, expired={}", seatId, LocalDateTime.now(), expireAt, expired);

                    if (expired) {
                        redisTemplate.opsForHash().put(redisKey, seatIdStr, "AVAILABLE");
                        redisTemplate.delete(holdKey);
                        log.info("[{}] 만료됨 → 상태 복구 완료", seatId);

                        expiredSeatsByUser
                                .computeIfAbsent(userId, k -> new ArrayList<>())
                                .add(seatId);
                    }
                } catch (Exception e) {
                    log.warn("복구 파싱 실패: key={}, value={}", holdKey, raw, e);
                }
            }

            // Kafka 발행
            for (Map.Entry<UUID, List<UUID>> entry : expiredSeatsByUser.entrySet()) {
                UUID userId = entry.getKey();
                List<UUID> expiredSeats = entry.getValue();
                seatEventProducer.sendSeatExpired(expiredSeats, userId);
                log.info("Kafka 발행 완료 - userId={}, expiredSeats={}", userId, expiredSeats);
            }
        }
    }
}
