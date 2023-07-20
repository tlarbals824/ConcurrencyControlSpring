package com.sim.redisconcurrency.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisDistributedLock {

    /**
     * 락 이름
     */
    String key();


    /**
     * 락 시간 단위
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 락 대기 시간
     */
    long waitTime() default 5L;

    /**
     * 락 유지 시간
     */
    long leaseTime() default 5L;
}
