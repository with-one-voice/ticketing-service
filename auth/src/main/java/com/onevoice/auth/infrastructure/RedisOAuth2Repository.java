package com.onevoice.auth.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisOAuth2Repository implements
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String KEY_PREFIX = "oauth2_auth_request:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final Duration expiration = Duration.ofMinutes(5);

    private String buildKey(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        return KEY_PREFIX + sessionId;
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String key = buildKey(request);
        log.debug("loadAuthorizationRequest: {}", key);
        try {
            return (OAuth2AuthorizationRequest) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis 연산 중 오류 발생: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
        HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }
        String key = buildKey(request);
        log.debug("saveAuthorizationRequest: {}", key);
        redisTemplate.opsForValue().set(key, authorizationRequest, expiration);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
        HttpServletResponse response) {
        String key = buildKey(request);
        log.debug("removeAuthorizationRequest: {}", key);
        OAuth2AuthorizationRequest requestObj = (OAuth2AuthorizationRequest) redisTemplate.opsForValue()
            .get(key);
        try {
            redisTemplate.delete(key);
            return requestObj;
        } catch (Exception e) {
            log.error("Redis 연산 중 오류 발생: {}", e.getMessage(), e);
            return requestObj; // 에러가 발생해도 요청 객체는 반환
        }
    }
}

