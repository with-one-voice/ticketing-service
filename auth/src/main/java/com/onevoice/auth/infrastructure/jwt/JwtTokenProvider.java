package com.onevoice.auth.infrastructure.jwt;

import com.onevoice.common.security.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private final long expirationMillis;
    private final Key secretKey;

    public JwtTokenProvider(
        @Value("${jwt.secret}") String secretKeyPlain,
        @Value("${jwt.expiration}") long expirationMillis
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyPlain.getBytes());
        this.expirationMillis = expirationMillis;
    }

    /**
     * AccessToken 생성
     */
    public String createAccessToken(UUID userId, UserRole role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .claim("role", role.roleName())
            .setExpiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }
}
