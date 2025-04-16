package com.onevoice.show.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onevoice.show.presentation.dto.response.SessionDetailResponseDto;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j(topic = "SessionCacheStore")
@Component
@RequiredArgsConstructor
public class SessionCacheStore {

    private final RedisTemplate<String, SessionDetailResponseDto> sessionDetailRedisTemplate;
    private final ObjectMapper objectMapper;

    private static final String SESSION_DETAIL_CACHE_PREFIX = "sessionDetail::";
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(5);

    public void saveSessionDetail(UUID sessionId, SessionDetailResponseDto dto) {
        String redisKey = SESSION_DETAIL_CACHE_PREFIX + sessionId;
        sessionDetailRedisTemplate.opsForValue().set(redisKey, dto, DEFAULT_TTL);
        log.info("Saved sessionDetail to Redis. key={}, ttl={}min", redisKey,
            DEFAULT_TTL.toMinutes());
    }

    public SessionDetailResponseDto getSessionDetail(UUID sessionId) {
        String redisKey = SESSION_DETAIL_CACHE_PREFIX + sessionId;

        Object raw = sessionDetailRedisTemplate.opsForValue().get(redisKey);
        SessionDetailResponseDto dto = null;

        if (raw != null) {
            try {
                String json = objectMapper.writeValueAsString(raw); // 1. LinkedHashMap -> JSON 문자열
                SessionCache cache = objectMapper.readValue(json,
                    SessionCache.class); // 2. JSON -> class
                dto = SessionCache.toDto(cache); // 3. class -> record
            } catch (Exception e) {
                log.error("Redis 역직렬화 실패. key={}, raw={}", redisKey, raw);
            }
        }

        log.info("Retrieved sessionDetail from Redis. key={}, hit={}", redisKey, dto != null);
        return dto;
    }

    public void deleteSessionDetail(UUID sessionId) {
        String redisKey = SESSION_DETAIL_CACHE_PREFIX + sessionId;
        Boolean deleted = sessionDetailRedisTemplate.delete(redisKey);
        log.info("Deleted sessionDetail from Redis. key={}, deleted={}", redisKey, deleted);
    }
}
