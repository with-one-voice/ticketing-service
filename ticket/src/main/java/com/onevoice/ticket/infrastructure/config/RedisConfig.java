package com.onevoice.ticket.infrastructure.config;

import com.onevoice.common.config.manualConfig.RedisSupportFactory;

import com.onevoice.common.config.manualConfig.RedissonSupportFactory;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisConfig {

    @Value("${redis.host}")
    private static String REDIS_HOST;

    @Value("${redis.port}")
    private static int REDIS_PORT;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return RedisSupportFactory.redisConnectionFactory(REDIS_HOST, REDIS_PORT);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        return RedisSupportFactory.redisTemplate(factory);
    }

    @Bean
    public RedissonClient redissonClient() {
        return RedissonSupportFactory.create(REDIS_HOST,REDIS_PORT);
    }

}
