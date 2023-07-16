package com.sim.redisconcurrency.service;

import com.sim.redisconcurrency.domain.Stock;
import com.sim.redisconcurrency.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        // get stock
        Stock stock = stockRepository.findByProductId(id).orElseThrow(() -> new IllegalArgumentException("재고가 없습니다."));
        // 재고감소
        stock.decrease(quantity);
        //저장
        stockRepository.saveAndFlush(stock);
    }
}
