package com.sim.redisconcurrency.aop.redisclient;

import java.util.concurrent.TimeUnit;

public interface RedisClient {
    void tryLock(String key, long waitTime, long usingTime, TimeUnit timeUnit) throws InterruptedException;

    void releaseLock(String key);
}
