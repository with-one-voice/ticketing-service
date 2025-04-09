package com.onevoice.notification.infrastructure.config;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class JpaAuditAware implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(authentication -> {
                Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
                boolean isUser = !auth.isEmpty();

                if (isUser) {
                    return (UUID) authentication.getPrincipal();
                }
                return null;
            });
    }

}
