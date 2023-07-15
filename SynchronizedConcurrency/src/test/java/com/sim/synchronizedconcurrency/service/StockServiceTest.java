package com.sim.synchronizedconcurrency.service;

import com.sim.synchronizedconcurrency.domain.Stock;
import com.sim.synchronizedconcurrency.repository.StockRepository;
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
        Stock stock = new Stock(1L, 100L);

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

    /**
     * expected: <0> but was: <94>
     * Expected :0
     * Actual   :94
     * Race Condition 발생 (경쟁상태) : 여러 스레드가 동시에 접근할 때, 의도하지 않은 결과가 발생하는 상황
     */
    @Test
    void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
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