package com.ys.Fintech.account.repository;

import com.ys.Fintech.account.domain.Account;
import com.ys.Fintech.account.domain.AccountStatus;
import com.ys.Fintech.accountUser.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
  Long countByAccountUser(AccountUser accountUser); // 회원 계정 개수
  boolean existsByAccountNum(String AccountNum);  // 계좌가 존재하는지

  List<Account> findByAccountStatusAndDeletedAtBefore(AccountStatus accountStatus, LocalDateTime localDateTime);  // 삭제된 계좌와 지정한 기간이 지난 데이터 조회
  Optional<List<Account>> findByAccountUserAndAccountStatusOrderByCreatedAtDesc(AccountUser accountUser, AccountStatus status); // 사용하는 계좌을 최신순으로 조회

  Optional<Account> findByAccountNumAndAccountStatus(String remittanceAccount, AccountStatus accountStatus);

  Optional<Account> findByAccountIdAndAccountStatus(Long accountId, AccountStatus accountStatus);
}
