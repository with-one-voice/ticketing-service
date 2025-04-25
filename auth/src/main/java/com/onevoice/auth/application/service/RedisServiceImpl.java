package com.onevoice.auth.application.service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final Duration TTL = Duration.ofMinutes(5); // 5분 유효

    public String store(String jwt) {
        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(sessionId, jwt, TTL);
        return sessionId;
    }

    public Optional<String> get(String sessionId) {
        String jwt = redisTemplate.opsForValue().get(sessionId);
        return Optional.ofNullable(jwt);
    }

    public void remove(String sessionId) {
        redisTemplate.delete(sessionId);
    }

    public void blacklistToken(String token, long expirationMillis) {
        redisTemplate.opsForValue()
            .set("blacklist:" + token, "logout", Duration.ofMillis(expirationMillis));
    }
}