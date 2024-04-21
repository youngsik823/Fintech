package com.ys.Fintech.accountUser.service;

import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.domain.Role;
import com.ys.Fintech.accountUser.dto.request.SignUpRequestDTO;
import com.ys.Fintech.accountUser.dto.response.SignUpResponseDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class AccountUserServiceTest {
  @Autowired
  AccountUserService accountUserService;

  @Test
  @DisplayName("회원가입 테스트")
  void signUp() {
    //given
    SignUpRequestDTO data = new SignUpRequestDTO("young@naver.com", "dudtlr123", "윤영식", "01012345678", Role.USER);
    try {
      accountUserService.SignUp(data);
      // 이메일 또는 이름이 존재할 경우 false
      SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO("ys@naver.com", "dkdltm1", "아이스", "01099281245", Role.USER);
      SignUpResponseDTO result = accountUserService.SignUp(signUpRequestDTO);
      System.out.println(result);
    } catch (Exception e) {
      throw new RuntimeException("이메일 또는 전화번호가 존재 합니다.");
    }
    //when
    //then
  }
}