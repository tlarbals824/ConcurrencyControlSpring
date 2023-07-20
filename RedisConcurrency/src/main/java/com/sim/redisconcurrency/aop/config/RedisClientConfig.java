package com.sim.redisconcurrency.aop.config;

import com.sim.redisconcurrency.aop.redisclient.LettuceRedisClient;
import com.sim.redisconcurrency.aop.redisclient.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

//@Configuration(proxyBeanMethods = false)
@Configuration
@RequiredArgsConstructor
public class RedisClientConfig {

    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    @Primary
    public RedisClient redisClient() {
        return new LettuceRedisClient(redisTemplate);
    }
}
