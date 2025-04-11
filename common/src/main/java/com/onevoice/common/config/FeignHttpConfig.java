package com.onevoice.common.config;

import feign.Client;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignHttpConfig {

    @Bean
    public Client feignClient() {
        return new OkHttpClient();
    }
}
