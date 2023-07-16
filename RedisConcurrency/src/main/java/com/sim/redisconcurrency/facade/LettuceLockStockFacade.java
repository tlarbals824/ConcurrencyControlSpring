package com.sim.redisconcurrency.facade;

import com.sim.redisconcurrency.repository.RedisLockRepository;
import com.sim.redisconcurrency.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;


    /**
     * 구현이 간단하다
     * spring data redis 를 이용하면 lettuce 가 기본이기때문에 별도의 라이브러리를 사용하지 않아도 된다.
     * spin lock 방식이기때문에 동시에 많은 스레드가 lock 획득 대기 상태라면 redis 에 부하가 갈 수 있다.
     *
     *
     * 재시도가 필요하지 않은 lock 은 lettuce 활용
     * 재시도가 필요한 경우에는 redisson 를 활용
     */
    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(id)) {
            Thread.sleep(5);
        }

        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }
    }
}
