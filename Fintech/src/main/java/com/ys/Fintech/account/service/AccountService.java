package com.ys.Fintech.account.service;

import com.ys.Fintech.account.domain.Account;
import com.ys.Fintech.account.domain.AccountBank;
import com.ys.Fintech.account.domain.AccountStatus;
import com.ys.Fintech.account.dto.request.DeleteAccountRequestDTO;
import com.ys.Fintech.account.dto.response.CreateAccountResponseDTO;
import com.ys.Fintech.account.dto.response.GetAccountListResponseDTO;
import com.ys.Fintech.account.dto.response.GetAccountResponseDTO;
import com.ys.Fintech.account.repository.AccountRepository;
import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.repository.AccountUserRepository;
import com.ys.Fintech.exception.CustomException;
import com.ys.Fintech.exception.ErrorCode;
import com.ys.Fintech.security.TokenAccountUserInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountService {
  private final AccountRepository accountRepository;
  private final AccountUserRepository accountUserRepository;
  private final PasswordEncoder encoder;

  // 계좌 생성
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
      String middleBankNum = String.format("%04d", random.nextInt(10000));
      String lastBankNum = String.format("%05d", random.nextInt(100000));
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

  // 계좌 상태 UN_USED 바꾸고 저장
  public boolean deleteAccount(DeleteAccountRequestDTO deleteAccountRequestDTO, TokenAccountUserInfo accountUserInfo) {
    AccountUser accountUser = accountUserRepository.findById(accountUserInfo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_USER));
    if (!accountUser.getEmail().equals(deleteAccountRequestDTO.getEmail())) {
      throw new CustomException(ErrorCode.NOT_ACCORD_USER_EMAIL);
    }
    String encodedPassword = accountUser.getPassword();
    if (!encoder.matches(deleteAccountRequestDTO.getPassword(), encodedPassword)) {
      throw new CustomException(ErrorCode.NOT_ACCORD_USER_PASSWORD);
    }
    Account account = accountRepository.findById(deleteAccountRequestDTO.getAccountId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_ACCOUNT));
    if (account.getAccountAmount() > 0) { // 잔액이 남아있을 경우
      throw new CustomException(ErrorCode.EXISTS_ACCOUNT_AMOUNT);
    }
    if (account.getAccountStatus().equals(AccountStatus.UN_USED)) {
      throw new CustomException(ErrorCode.ALREADY_ACCOUNT_STATUS_UN_USED);
    }

    accountRepository.save(Account.builder()
        .accountId(account.getAccountId())
        .accountUser(account.getAccountUser())
        .accountBank(account.getAccountBank())
        .accountNum(account.getAccountNum())
        .accountAmount(account.getAccountAmount())
        .accountStatus(AccountStatus.UN_USED)
        .deletedAt(LocalDateTime.now())
        .build());

    return true;
  }

  // 계좌 식제
  @Async
  @Scheduled(cron = "0 0 0 * * *")   // 매일 자정에 실행
  public void AutomaticDelete() {  // 일정 기간이 지나면 자동 삭제
    List<Account> unUsedAccounts = accountRepository.findByAccountStatusAndDeletedAtBefore(AccountStatus.UN_USED, LocalDateTime.now().minusMonths(1));// 한달 전 데이터 조회하고 계정 상태가 UN_USED면 삭제
    for (Account account : unUsedAccounts) {
      accountRepository.delete(account);
    }
  }

  // 계좌 조회
  public GetAccountListResponseDTO getAccount(TokenAccountUserInfo accountUserInfo) {
    AccountUser accountUser = accountUserRepository.findById(accountUserInfo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_USER));
    List<Account> accounts = accountRepository.findByAccountUserAndAccountStatusOrderByCreatedAtDesc(accountUser, AccountStatus.USED) // userId와 AccountStatus.USED 가 맞을경우 최신 순으로 조회
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_ACCOUNT));
    List<GetAccountResponseDTO> accountList = accounts.stream()
        .map(GetAccountResponseDTO::new)  // account -> new GetAccountResponseDTO(account) Account 를 getAccountResponseDTO 로 변환
        .collect(Collectors.toList());  // 리스트로 정렬

    return GetAccountListResponseDTO.builder()
        .accountList(accountList)
        .build();
  }
}
