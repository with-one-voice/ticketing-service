package com.onevoice.common.config.manualConfig;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonSupportFactory {

    public static RedissonClient create(String host, int port) {
        Config config = new Config();
        config.useSingleServer()
            .setAddress("redis://" + host + ":" + port)
            .setConnectionMinimumIdleSize(5)
            .setConnectionPoolSize(10);
        return Redisson.create(config);
    }
}