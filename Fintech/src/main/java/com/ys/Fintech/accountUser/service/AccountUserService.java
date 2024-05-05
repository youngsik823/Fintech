package com.ys.Fintech.accountUser.service;

import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.dto.request.AccountUserDeleteRequestDTO;
import com.ys.Fintech.accountUser.dto.request.AccountUserModifyRequestDTO;
import com.ys.Fintech.accountUser.dto.request.SignInRequestDTO;
import com.ys.Fintech.accountUser.dto.request.SignUpRequestDTO;
import com.ys.Fintech.accountUser.dto.response.AccountUserModifyResponseDTO;
import com.ys.Fintech.accountUser.dto.response.SignInResponseDTO;
import com.ys.Fintech.accountUser.dto.response.SignUpResponseDTO;
import com.ys.Fintech.accountUser.repository.AccountUserRepository;
import com.ys.Fintech.exception.CustomException;
import com.ys.Fintech.exception.ErrorCode;
import com.ys.Fintech.security.TokenAccountUserInfo;
import com.ys.Fintech.security.TokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountUserService implements UserDetailsService {
  private final AccountUserRepository accountUserRepository;
  private final PasswordEncoder encoder;
  private final TokenProvider tokenProvider;

  @Override
  public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
    return accountUserRepository.findByName(name)
        .orElseThrow(() -> new UsernameNotFoundException("couldn't find accountUser -> " + name));
  }

  // 회원가입
  public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {
    if (signUpRequestDTO == null) {
      throw new CustomException(ErrorCode.MISSING_REQUEST_DTO_ERROR);
    }
    // 이메일, 전화번호 중복 확인
    String email = signUpRequestDTO.getEmail();
    String phoneNum = signUpRequestDTO.getPhoneNum();
    boolean exists = accountUserRepository.existsByEmailOrPhoneNum(email, phoneNum);
    if (exists) {
      throw new CustomException(ErrorCode.EXISTS_USER_EMAIL_OR_PHONE_NUM);
    }
    String encode = encoder.encode(signUpRequestDTO.getPassword()); // 비밀번호를 암호화
    signUpRequestDTO.setPassword(encode);

    AccountUser save = accountUserRepository.save(signUpRequestDTO.toEntity());
    return new SignUpResponseDTO(save);
  }

  // 로그인 검증
  public SignInResponseDTO verifyLogin(SignInRequestDTO signInRequestDTO){
  AccountUser accountUser = accountUserRepository.findByEmail(signInRequestDTO.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_USER_EMAIL));

    // 패스워드 검증
    String inputPassword = signInRequestDTO.getPassword();  // 입력 비번
    String encoderPassword = accountUser.getPassword(); // db에 저장된 비번

    if (!encoder.matches(inputPassword, encoderPassword)) {
      throw new CustomException(ErrorCode.NOT_ACCORD_USER_PASSWORD);
    }

    String token = tokenProvider.createToken(accountUser);
    return new SignInResponseDTO(accountUser, token);
  }

  // 회원 정보 수정 (이름, 전화번호)
  public AccountUserModifyResponseDTO accountUserModify(AccountUserModifyRequestDTO accountUserModifyRequestDTO, TokenAccountUserInfo accountUserInfo) {

    AccountUser accountUser = accountUserRepository.findById(accountUserInfo.getId()).orElseThrow(
        () -> new CustomException(ErrorCode.NOT_EXISTS_USER));
    AccountUser modifyAccountUser = accountUserModifyRequestDTO.modifyAccountUser(accountUser, accountUserModifyRequestDTO);
    AccountUser save = accountUserRepository.save(modifyAccountUser);
    return new AccountUserModifyResponseDTO(save);
  }


  // 회원 정보 삭제
  public boolean accountUserDelete(AccountUserDeleteRequestDTO accountUserDeleteRequestDTO, TokenAccountUserInfo accountUserInfo) {

    AccountUser accountUser = accountUserRepository.findById(accountUserInfo.getId()).orElseThrow(
        () -> new CustomException(ErrorCode.NOT_EXISTS_USER));

    if (!accountUser.getEmail().equals(accountUserDeleteRequestDTO.getEmail())) {
      throw new CustomException(ErrorCode.NOT_EXISTS_USER_EMAIL);
    }

    String encodedPassword = accountUser.getPassword(); // 현제 db에 저장된 값
    if (!encoder.matches(accountUserDeleteRequestDTO.getPassword(), encodedPassword)) {
      throw new CustomException(ErrorCode.NOT_ACCORD_USER_PASSWORD);
    }
    accountUserRepository.deleteById(accountUserInfo.getId());
    return true;
  }
}
