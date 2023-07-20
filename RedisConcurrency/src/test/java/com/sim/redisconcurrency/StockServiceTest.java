package com.sim.redisconcurrency;

import com.sim.redisconcurrency.domain.Stock;
import com.sim.redisconcurrency.repository.StockRepository;
import com.sim.redisconcurrency.service.StockService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void init() {
        Stock stock = new Stock(1L, 1000L);

        stockRepository.saveAndFlush(stock);
    }

    @AfterEach
    public void tearDown() {
        stockRepository.deleteAll();
    }

    @Test
    void stock_decrease() {
        stockService.decrease(1L, 1L);

        // 100 -1 = 99
        Stock stock = stockRepository.findByProductId(1L).orElseThrow(() -> new IllegalArgumentException("재고가 없습니다."));

        assertEquals(99, stock.getQuantity());
    }

    // 테스트 시간 : 754ms
    @Test
    void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);


        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findByProductId(1L).orElseThrow(() -> new IllegalArgumentException("재고가 없습니다."));

        assertEquals(0, stock.getQuantity());
    }
}