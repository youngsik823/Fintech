package com.ys.Fintech.accountUser.repository;

import com.ys.Fintech.accountUser.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {
  boolean existsByEmailOrPhoneNum(String email, String phoneNum);
  Optional<AccountUser> findByName(String name);
}
