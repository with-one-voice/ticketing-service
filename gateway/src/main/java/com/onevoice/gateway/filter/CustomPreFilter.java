package com.onevoice.gateway.filter;

import com.onevoice.gateway.jwt.JwtUtils;
import java.util.UUID;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Component
public class CustomPreFilter implements GlobalFilter, Ordered {

    private static final Logger logger = Logger.getLogger(CustomPreFilter.class.getName());

    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String uri = exchange.getRequest().getURI().getPath().toString();
        log.info("PreFilter request uri :{}",uri);

        if (uri.startsWith("/api/auth/signup") || uri.startsWith("/api/auth/login")) {
            return chain.filter(exchange);
        }

        String token = resolveToken(exchange.getRequest().getHeaders());
        if (token == null || !jwtUtils.validateToken(token)) {
            // 토큰이 없거나 유효하지 않음 -> 401 Unauthorized 응답
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 토큰 유효 → 헤더에 userId, role 추가
        UUID userId = jwtUtils.getUserId(token);
        String role = jwtUtils.getUserRole(token);

        ServerWebExchange mutatedExchange = exchange.mutate()
            .request(builder -> builder
                .header("X-User-Id", userId.toString())
                .header("X-User-Role", role)
            )
            .build();

        return chain.filter(mutatedExchange);
    }

    private String resolveToken(HttpHeaders headers) {
        String bearer = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
