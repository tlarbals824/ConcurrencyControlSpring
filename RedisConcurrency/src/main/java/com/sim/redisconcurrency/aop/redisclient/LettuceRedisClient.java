package com.sim.redisconcurrency.aop.redisclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class LettuceRedisClient implements RedisClient{

    private static final String LOCK_VALUE = "lock";
    private static final String TIME_OUT_MESSAGE = "Time out";

    private final RedisTemplate<String, String> redisTemplate;
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void tryLock(final String key, long waitTime, long usingTime,TimeUnit timeUnit) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while(true) {
            long currentTime = System.currentTimeMillis();
            if(currentTime-startTime > timeUnit.toMillis(waitTime)) throw new InterruptedException(TIME_OUT_MESSAGE);

            if(Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, LOCK_VALUE, Duration.ofMillis(timeUnit.toMillis(usingTime))))){
                break;
            }
            Thread.sleep(20);
        }
        int incremented = atomicInteger.incrementAndGet();
        log.info("get Lock!: {}",incremented);
    }

    @Override
    public void releaseLock(String key) {
        redisTemplate.delete(key);
        log.info("release Lock!");
    }
}
