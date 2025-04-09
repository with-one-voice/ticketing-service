package com.onevoice.notification.infrastructure.config;

import feign.Logger;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "FeignConfig")
@Configuration
@EnableFeignClients(basePackages = "com.onevoice.notification")
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
                HttpServletRequest request = servletRequestAttributes.getRequest();

                // 클라이언트 요청에서 username 과 role 헤더를 가져와 Feign 요청 헤더에 추가
                String username = request.getHeader("X-User-Id");
                String role = request.getHeader("X-User-Role");

                log.info("username:{},role:{}", username, role);

                if (username != null) {
                    template.header("X-User-Id", username);
                }
                if (role != null) {
                    template.header("X-User-Role", role);
                }
            }
        };
    }
}
