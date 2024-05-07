package com.ys.Fintech.account.service;

import com.ys.Fintech.account.domain.Account;
import com.ys.Fintech.account.domain.AccountBank;
import com.ys.Fintech.account.domain.AccountStatus;
import com.ys.Fintech.account.dto.response.CreateAccountResponseDTO;
import com.ys.Fintech.account.repository.AccountRepository;
import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.repository.AccountUserRepository;
import com.ys.Fintech.exception.CustomException;
import com.ys.Fintech.exception.ErrorCode;
import com.ys.Fintech.security.TokenAccountUserInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountService {
  private final AccountRepository accountRepository;
  private final AccountUserRepository accountUserRepository;

  // 계정 생성
  public CreateAccountResponseDTO createAccount(TokenAccountUserInfo accountUserInfo) {
    AccountUser accountUser = accountUserRepository.findById(accountUserInfo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_USER));
    Long accountNum = accountRepository.countByAccountUser(accountUser);

    if (accountNum >= 5) {  // 계정이 5개 존재하면 예외처리
      throw new CustomException(ErrorCode.EXCEED_ACCOUNT_NUM);
    }
    String newAccountNum = accountRepository.findFirstByOrderByAccountIdDesc()
        .map(account -> (Integer.parseInt(account.getAccountNum())) + 1 + "")
        .orElse("1000000000");
    Account account = accountRepository.save(
        Account.builder()
            .accountUser(accountUser)
            .accountAmount(0L)
            .accountBank(AccountBank.APPLE)
            .accountStatus(AccountStatus.USED)
            .accountNum(newAccountNum)
            .build()
    );

    return CreateAccountResponseDTO.toEntity(account);
  }
}
