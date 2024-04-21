package com.ys.Fintech.accountUser.service;

import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.dto.request.SignUpRequestDTO;
import com.ys.Fintech.accountUser.dto.response.SignUpResponseDTO;
import com.ys.Fintech.accountUser.repository.AccountUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountUserService {
  private final AccountUserRepository accountUserRepository;

  // 회원가입
  public SignUpResponseDTO SignUp(SignUpRequestDTO signUpRequestDTO) throws Exception {
    if (signUpRequestDTO == null) {
      throw new RuntimeException("가입 정보가 없습니다.");
    }
    // 이메일, 전화번호 중복 확인
    String email = signUpRequestDTO.getEmail();
    String phoneNum = signUpRequestDTO.getPhoneNum();
    boolean exists = accountUserRepository.existsByEmailOrPhoneNum(email, phoneNum);
    if (exists) {
      throw new Exception("이미 가입된 회원이 있습니다.");
    }
    AccountUser save = accountUserRepository.save(signUpRequestDTO.toEntity());
    return new SignUpResponseDTO(save);
  }

}
