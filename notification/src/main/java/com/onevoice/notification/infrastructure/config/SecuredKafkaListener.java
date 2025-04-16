package com.onevoice.notification.infrastructure.config;

import java.util.List;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * KafkaListener 가 실행될 때는 인증정보가 없으므로 토픽 메시지에 userId를 전달해 security context 를 임의로 만들어 줄 수 있도록 구성한다.
 */
public abstract class SecuredKafkaListener {

    protected void withSecurityContext(UUID userId, Runnable runnable) {
        try {
            setSecurityContext(userId);
            runnable.run();
        } finally {
            // Security Context 정리
            SecurityContextHolder.clearContext();
        }
    }

    private void setSecurityContext(UUID userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        context.setAuthentication(
            new UsernamePasswordAuthenticationToken(userId, null, authorities));
        SecurityContextHolder.setContext(context);
    }
}
