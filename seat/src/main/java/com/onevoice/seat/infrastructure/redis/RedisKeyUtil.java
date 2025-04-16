package com.onevoice.seat.infrastructure.redis;

import java.util.UUID;

public class RedisKeyUtil {

    private RedisKeyUtil() {
        throw new IllegalStateException("Utility class");
    }


    public static String seatStatusKey(UUID sessionId) {
        return "seat:" + sessionId;
    }

    public static String seatHoldKey(UUID sessionId, UUID seatId) {
        return "seat-hold:" + sessionId + ":" + seatId;
    }

    public static String seatLockKey(UUID seatId) {
        return "lock:seat:" + seatId;
    }

    public static String seatCacheKey(UUID sessionId) {
        return "seat-cache:" + sessionId;
    }
}
