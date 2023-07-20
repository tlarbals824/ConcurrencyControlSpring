package com.sim.redisconcurrency.aop.redisclient;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonRedisClient implements RedisClient {

    private final RedissonClient redissonClient;

    @Override
    public void tryLock(String key, long waitTime, long usingTime, TimeUnit timeUnit) throws InterruptedException {
        final RLock lock = redissonClient.getLock(key);
        boolean available = lock.tryLock(waitTime, usingTime, timeUnit);
        if (!available) throw new InterruptedException();
    }

    @Override
    public void releaseLock(String key) {
        final RLock lock = redissonClient.getLock(key);
        lock.unlock();
    }
}
