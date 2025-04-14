package com.onevoice.common.security;

import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
        jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
        throws ServletException, IOException {

        String uri = request.getRequestURI();
        if (!uri.startsWith("/actuator")) {
            log.info("Request URI :{}", uri);
        }

        String userIdHeader = request.getHeader("X-User-Id");
        String roleHeader = request.getHeader("X-User-Role");

        if (userIdHeader != null && roleHeader != null) {
            UUID userId = UUID.fromString(userIdHeader);
            UserRole role = UserRole.from(roleHeader);

            List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(role.roleName()));

            Authentication auth = new UsernamePasswordAuthenticationToken(userId, null,
                authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        filterChain.doFilter(request, response);
    }

}
