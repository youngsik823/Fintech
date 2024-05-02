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
  public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) throws Exception {
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
    String encode = encoder.encode(signUpRequestDTO.getPassword()); // 비밀번호를 암호화
    signUpRequestDTO.setPassword(encode);

    AccountUser save = accountUserRepository.save(signUpRequestDTO.toEntity());
    return new SignUpResponseDTO(save);
  }

  // 로그인 검증
  public SignInResponseDTO verifyLogin(SignInRequestDTO signInRequestDTO){
  AccountUser accountUser = accountUserRepository.findByEmail(signInRequestDTO.getEmail())
        .orElseThrow(() -> new RuntimeException("가입된 회원이 아닙니다."));

    // 패스워드 검증
    String inputPassword = signInRequestDTO.getPassword();  // 입력 비번
    String encoderPassword = accountUser.getPassword(); // db에 저장된 비번

    if (!encoder.matches(inputPassword, encoderPassword)) {
      throw new RuntimeException("비밀번호가 틀렸습니다.");
    }

    String token = tokenProvider.createToken(accountUser);
    log.info("Login token -- {}", token);
    return new SignInResponseDTO(accountUser, token);
  }

  // 회원 정보 수정 (이름, 전화번호)
  public AccountUserModifyResponseDTO accountUserModify(AccountUserModifyRequestDTO accountUserModifyRequestDTO, TokenAccountUserInfo accountUserInfo) {

    AccountUser accountUser = accountUserRepository.findById(accountUserInfo.getId()).orElseThrow(
        () -> new RuntimeException("회원이 존재하지 않습니다."));
    log.info("accountUser----{}", accountUser);
    AccountUser modifyAccountUser = accountUserModifyRequestDTO.modifyAccountUser(accountUser, accountUserModifyRequestDTO);
    log.info("modifyAccountUser---------{}", modifyAccountUser);
    AccountUser save = accountUserRepository.save(modifyAccountUser);
    log.info("save---- {}", save);
    return new AccountUserModifyResponseDTO(save);
  }


  // 회원 정보 삭제
  public boolean accountUserDelete(AccountUserDeleteRequestDTO accountUserDeleteRequestDTO, TokenAccountUserInfo accountUserInfo) {

    AccountUser accountUser = accountUserRepository.findById(accountUserInfo.getId()).orElseThrow(
        () -> new RuntimeException("회원이 존재하지 않습니다.")
    );
    if (!accountUser.getEmail().equals(accountUserDeleteRequestDTO.getEmail())) {
      throw new RuntimeException("이메일이 일치하지 않습니다.");
    }

    String encodedPassword = accountUser.getPassword(); // 현제 db에 저장된 값
    if (!encoder.matches(accountUserDeleteRequestDTO.getPassword(), encodedPassword)) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }
    accountUserRepository.deleteById(accountUserInfo.getId());
    return true;
  }
}
