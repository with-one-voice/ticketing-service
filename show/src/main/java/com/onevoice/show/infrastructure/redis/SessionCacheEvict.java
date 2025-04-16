package com.onevoice.show.infrastructure.redis;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Slf4j(topic = "SessionCacheEvict")
@Component
@RequiredArgsConstructor
public class SessionCacheEvict {

    private final CacheManager cacheManager;

    public void evictSessionDetail(UUID sessionId) {
        Cache cache = cacheManager.getCache("sessionDetail");
        if (cache != null) {
            cache.evict(sessionId);
            log.info("Evicted from 'sessionDetail' with key: {}", sessionId);
        }
    }

    public void evictShowSessions(UUID showId) {
        Cache cache = cacheManager.getCache("showSessions");
        if (cache != null) {
            cache.evict(showId);
            log.info("Evicted from 'showSessions' with key: {}", showId);
        }
    }
}
