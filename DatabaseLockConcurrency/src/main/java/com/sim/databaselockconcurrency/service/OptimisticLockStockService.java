package com.sim.databaselockconcurrency.service;

import com.sim.databaselockconcurrency.domain.Stock;
import com.sim.databaselockconcurrency.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockService {
    private final StockRepository stockRepository;

    /**
     * 낙관적 락(Optimistic Lock) 장점:
     * 별도의 Lock을 걸지 않기 때문에 성능상의 이점이 있습니다.
     *
     * 단점 :
     * 업데이트 실패시 재시도 로직을 작성해줘야함
     * 충돌이 빈번하게 일어난다면 비관적 락이 더 좋은 선택일 수 있음
     */
    @Transactional
    public void decrease(Long id, Long quantity){
        // get stock
        Stock stock = stockRepository.findByProductIdWithOptimisticLock(id).orElseThrow(() -> new IllegalArgumentException("재고가 없습니다."));
        // 재고감소
        stock.decrease(quantity);
        //저장
        stockRepository.saveAndFlush(stock);
    }
}
