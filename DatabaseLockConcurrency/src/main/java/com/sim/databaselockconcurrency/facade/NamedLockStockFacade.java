package com.sim.databaselockconcurrency.facade;

import com.sim.databaselockconcurrency.repository.LockRepository;
import com.sim.databaselockconcurrency.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {

    private final LockRepository lockRepository;

    private final StockService stockService;


    /**
     * 네임드 락(Named Lock) 은 분산락을 구현할 때 사용함
     * 데이터 삽입시에 정합성을 맞추기 위해 사용함
     * 하지만 락 해제와 세션 관리를 잘해야함
     */
    @Transactional
    public void decrease(Long id, Long quantity){
        try{
            lockRepository.getLock(id.toString());
            stockService.decrease(id, quantity);
        }finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
