package com.sim.databaselockconcurrency.service;

import com.sim.databaselockconcurrency.domain.Stock;
import com.sim.databaselockconcurrency.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PessimisticLockStockService {

    private final StockRepository stockRepository;

    /**
     * 비관적 락(Pessimistic Lock) 장점
     * 충돌이 빈번하게 발생할 경우 낙관적 락(Optimistic Lock)보다 성능이 좋을 수 있음
     * 데이터 정합성이 보장됨
     *
     * 단점 :
     * 별도의 Lock을 걸기 때문에 성능 저하가 있을 수 있음
     */
    @Transactional
    public void decrease(Long id, Long quantity){
        Stock stock = stockRepository.findByProductIdWithPessimisticLock(id).orElseThrow(() -> new IllegalArgumentException("재고가 없습니다."));

        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
