package com.sim.redisconcurrency.service;

import com.sim.redisconcurrency.domain.Stock;
import com.sim.redisconcurrency.aop.RedisDistributedLock;
import com.sim.redisconcurrency.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    @Transactional
    @RedisDistributedLock(key = "stock#id", timeUnit = TimeUnit.SECONDS)
    public void decrease(Long id, Long quantity) {
        // get stock
        Stock stock = stockRepository.findByProductId(id).orElseThrow(() -> new IllegalArgumentException("재고가 없습니다."));
        // 재고감소
        stock.decrease(quantity);
        log.info("stock: {}", stock);
        //저장
        stockRepository.saveAndFlush(stock);
    }
}
