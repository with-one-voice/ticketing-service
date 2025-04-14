package com.onevoice.show.infrastructure.redis;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Long> redisTemplate;

    private static final String PREFIX = "show:view:";

    public void increaseShowViewCount(UUID showId) {
        String key = PREFIX + showId;
        redisTemplate.opsForValue().increment(key);
    }

    public Long getShowViewCount(UUID showId) {
        String key = PREFIX + showId;
        return Optional.ofNullable(redisTemplate.opsForValue().get(key)).orElse(0L);
    }

    public Set<String> getAllViewKeys() {
        return redisTemplate.keys(PREFIX + "*");
    }

    public UUID extractShowIdFromKey(String key) {
        return UUID.fromString(key.substring(PREFIX.length()));
    }

    public void deleteViewKey(String key) {
        redisTemplate.delete(key);
    }

    private String generateKey(UUID showId) {
        return PREFIX + showId;
    }
}
