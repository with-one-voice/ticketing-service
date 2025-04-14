package com.onevoice.notification.infrastructure.config;

import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j(topic = "FeignConfig")
@Configuration
@EnableFeignClients(basePackages = "com.onevoice.notification")
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
