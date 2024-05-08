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

import java.util.Random;

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
    Long accountCount = accountRepository.countByAccountUser(accountUser);

    if (accountCount >= 5) {  // 계정이 5개 존재하면 예외처리
      throw new CustomException(ErrorCode.EXCEED_ACCOUNT_NUM);
    }
    Random random = new Random();
    StringBuffer sb = new StringBuffer();
    String newAccountNum; // 새 계좌 번호
    while (true) {
      String AppleBankNum = "0823";
      String middleBankNum = String.format("%4d", random.nextInt(10000));
      String lastBankNum = String.format("%5d", random.nextInt(100000));
      newAccountNum = sb.append(AppleBankNum).append("-").append(middleBankNum).append("-").append(lastBankNum).toString();
      boolean exists = accountRepository.existsByAccountNum(newAccountNum);
      if (!exists) {
        break;
      }
    }


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
