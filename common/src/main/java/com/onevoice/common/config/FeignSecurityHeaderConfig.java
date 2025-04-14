package com.onevoice.common.config;


import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@Configuration
@Slf4j
public class FeignSecurityHeaderConfig {

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof UUID userId) {
                    String role = authentication.getAuthorities().stream()
                            .findFirst()
                            .map(GrantedAuthority::getAuthority)
                            .orElse("UNKNOWN");

                    requestTemplate.header("X-User-Id", userId.toString());
                    requestTemplate.header("X-User-Role", role);
                } else {
                    log.debug("Skipping header injection: principal is not UUID → {}", principal);
                }
            }
        };
    }

}