package com.onevoice.auth.application.service;

import java.util.Optional;

public interface RedisService {

    String store(String jwt);

    Optional<String> get(String sessionId);

    void remove(String sessionId);

    void blacklistToken(String token, long expirationMillis);
}
