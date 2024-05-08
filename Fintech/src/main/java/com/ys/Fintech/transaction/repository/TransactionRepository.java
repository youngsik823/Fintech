package com.ys.Fintech.transaction.repository;

import com.ys.Fintech.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  
}
