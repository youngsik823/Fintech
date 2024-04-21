package com.ys.Fintech.accountUser.repository;

import com.ys.Fintech.accountUser.domain.Role;
import com.ys.Fintech.accountUser.dto.request.SignUpRequestDTO;
import com.ys.Fintech.accountUser.service.AccountUserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@Transactional
class AccountUserRepositoryTest {
@Autowired
  AccountUserRepository accountUserRepository;
@Autowired
  AccountUserService accountUserService;

  @Test
  @DisplayName("이메일 또는 전화번호가 존재하는지 테스트")
  void existsEmailOrdPhoneNum() {
    try {
      SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("young@naver.com", "dudtlr123", "윤영식", "01012345678", Role.USER);
      accountUserService.SignUp(signUpRequestDTO);
      boolean exists = accountUserRepository.existsByEmailOrPhoneNum("young@naver.com", "01012345678");
      assertTrue(exists);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}