package com.sim.synchronizedconcurrency.service;

import com.sim.synchronizedconcurrency.domain.Stock;
import com.sim.synchronizedconcurrency.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    /**
     * @Transactional이 붙은 메소드는 프록시 패턴으로 생성된 클래스가 실행되는데 decrease 메소드가 완료된 이후에 커밋되는 시점의 차이로 인해 완전한 동시성을 보장하지 못한다.
     * 즉 decrease 완료 -> 다른 스레드에서 decrease 실행 -> 커밋 이 되기 때문에 동시성을 보장하지 못한다.
     */
//    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        // get stock
        Stock stock = stockRepository.findByProductId(id).orElseThrow(() -> new IllegalArgumentException("재고가 없습니다."));
        // 재고감소
        stock.decrease(quantity);
        //저장
        stockRepository.saveAndFlush(stock);
    }
}
