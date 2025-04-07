package com.onevoice.auth.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
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
    public String createAccessToken(UUID userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 토큰에서 userId(UUID) 추출
     */
    public UUID getUserId(String token) {
        String subject = parseClaims(token).getSubject();
        return UUID.fromString(subject);
    }

    /**
     * 토큰에서 Role 추출
     */
    public String getUserRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원하지 않는 JWT입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 클레임이 비어 있습니다.");
        }
        return false;
    }

    /**
     * Claims 파싱
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}