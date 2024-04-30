package com.ys.Fintech.accountUser.repository;

import com.ys.Fintech.accountUser.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {
  // 이메일과 전화번호가 존재하는지
  boolean existsByEmailOrPhoneNum(String email, String phoneNum);
  // 회원이 존재하는지
  Optional<AccountUser> findByName(String email);
  // 이메일로 회원정보 조회
  Optional<AccountUser> findByEmail(String email);
}
