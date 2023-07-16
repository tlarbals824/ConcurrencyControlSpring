package com.sim.databaselockconcurrency.repository;

import com.sim.databaselockconcurrency.domain.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductId(Long productId);

    /**
     * 비관적 락 설정
     */
    @Query("select s from Stock s where s.productId = :productId")
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Stock> findByProductIdWithPessimisticLock(Long productId);

    /**
     * 낙관적 락 설정
     */
    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.productId = :productId")
    Optional<Stock> findByProductIdWithOptimisticLock(Long productId);



}
