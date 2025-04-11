package com.onevoice.show.application.service;

import com.onevoice.show.domain.Session;
import com.onevoice.show.domain.Show;
import com.onevoice.show.domain.repository.SessionRepository;
import com.onevoice.show.domain.repository.ShowRepository;
import com.onevoice.show.infrastructure.redis.RedisService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShowScheduler {

    private final ShowRepository showRepository;
    private final SessionRepository sessionRepository;
    private final RedisService redisService;

    /**
     * 공연 상태 체크 (1분 간격으로 현재 날짜.시간과 티켓팅 닐짜.시간과 비교)
     */
    @Scheduled(fixedRate = 60000) // 1분마다
    @SchedulerLock(name = "updateShowSessionStatus", lockAtLeastFor = "PT30S", lockAtMostFor = "PT1M")
    @Transactional
    public void updateShowSessionStatus() {
        LocalDateTime now = LocalDateTime.now();

        // show 상태
        List<Show> shows = showRepository.findAll();
        for (Show show : shows) {
            show.updateStatusByTime(now);
        }

        // session 상태
        List<Session> sessions = sessionRepository.findAll(); // show -> fetch join
        for (Session session : sessions) {
            session.updateStatusByTime(now);
        }
    }

    /**
     * DB에 조회수 반영 (5분 간격으로 Redis에 저장된 값 DB에 저장)
     */
    @Scheduled(fixedRate = 300_000) // 5분마다 실행
    @SchedulerLock(name = "flushViewCountsToDb", lockAtLeastFor = "PT4M", lockAtMostFor = "PT5M")
    @Transactional
    public void flushViewCountsToDb() {
        Set<String> keys = redisService.getAllViewKeys();

        for (String key : keys) {
            UUID showId = redisService.extractShowIdFromKey(key);
            Long viewCount = redisService.getShowViewCount(showId);

            log.info("Flushing view count for showId={} -> {}", showId, viewCount);

            showRepository.updateViewCount(showId, viewCount); // DB에 저장
            redisService.deleteViewKey(key); // 캐시 초기화
        }
    }
}
