package com.ys.Fintech.account.repository;

import com.ys.Fintech.account.domain.Account;
import com.ys.Fintech.accountUser.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
  Long countByAccountUser(AccountUser accountUser); // 회원 계정 개수
  boolean existsByAccountNum(String AccountNum);  // 계좌가 존재하는지

}
