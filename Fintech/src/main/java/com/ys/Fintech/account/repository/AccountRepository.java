package com.ys.Fintech.account.repository;

import com.ys.Fintech.account.domain.Account;
import com.ys.Fintech.accountUser.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
  Optional<Account> findFirstByOrderByAccountIdDesc(); // 맨 마지막 값을 가져온다.
  Long countByAccountUser(AccountUser accountUser); // 회원 계정 개수
}
