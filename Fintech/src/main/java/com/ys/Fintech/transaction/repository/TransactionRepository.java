package com.ys.Fintech.transaction.repository;

import com.ys.Fintech.account.domain.Account;
import com.ys.Fintech.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Page<Transaction> findByAccount(Account account, PageRequest pageable);
}
