package com.sim.redisconcurrency.aop;

import com.sim.redisconcurrency.aop.redisclient.RedisClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class RedisConcurrencyAop {
    private static final String LOCK_PREFIX = "lock:";

    private final RedisClient redisClient;

    @Around("@annotation(RedisDistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisDistributedLock redisDistributedLock = method.getAnnotation(RedisDistributedLock.class);

        String key = LOCK_PREFIX+redisDistributedLock.key();
        try{
            redisClient.tryLock(key, redisDistributedLock.waitTime(), redisDistributedLock.leaseTime(),redisDistributedLock.timeUnit());

            return joinPoint.proceed();
        }catch (InterruptedException e){
            throw new InterruptedException();
        } finally {
            redisClient.releaseLock(key);
        }
    }

}
