package com.ys.Fintech.transaction.service;

import com.ys.Fintech.account.domain.Account;
import com.ys.Fintech.account.domain.AccountStatus;
import com.ys.Fintech.account.repository.AccountRepository;
import com.ys.Fintech.accountUser.domain.AccountUser;
import com.ys.Fintech.accountUser.repository.AccountUserRepository;
import com.ys.Fintech.exception.CustomException;
import com.ys.Fintech.exception.ErrorCode;
import com.ys.Fintech.security.TokenAccountUserInfo;
import com.ys.Fintech.transaction.domain.Transaction;
import com.ys.Fintech.transaction.domain.TransactionType;
import com.ys.Fintech.transaction.dto.page.PageDTO;
import com.ys.Fintech.transaction.dto.page.PageResponseDTO;
import com.ys.Fintech.transaction.dto.request.DepositTransactionRequestDTO;
import com.ys.Fintech.transaction.dto.request.WithdrawTransactionRequestDTO;
import com.ys.Fintech.transaction.dto.response.DepositTransactionResponseDTO;
import com.ys.Fintech.transaction.dto.response.TransactionListResponseDTO;
import com.ys.Fintech.transaction.dto.response.TransactionResponseDTO;
import com.ys.Fintech.transaction.dto.response.WithdrawTransactionResponseDTO;
import com.ys.Fintech.transaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TransactionService {
  private final AccountUserRepository accountUserRepository;
  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;
  private final PasswordEncoder encoder;

  public DepositTransactionResponseDTO depositTransaction(DepositTransactionRequestDTO depositTransactionRequestDTO, TokenAccountUserInfo accountUserInfo) {
    AccountUser accountUser = accountUserRepository.findById(accountUserInfo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_USER));
    String encodedPassword = accountUser.getPassword();
    if (!encoder.matches(depositTransactionRequestDTO.getPassword(), encodedPassword)) {
      throw new CustomException(ErrorCode.NOT_ACCORD_USER_PASSWORD);
    }
    Account account = accountRepository.findByAccountIdAndAccountStatus(depositTransactionRequestDTO.getAccountId(), AccountStatus.USED) // 본인 계좌
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_ACCOUNT));
    if (account.getAccountAmount() == 0 || account.getAccountAmount() < depositTransactionRequestDTO.getTransactionAmount()) { // 거래 잔액이 부족할때
      throw new CustomException(ErrorCode.NOT_ENOUGH_AMOUNT);
    }
    account.setAccountAmount(account.getAccountAmount() - depositTransactionRequestDTO.getTransactionAmount());
    accountRepository.save(account);
    Account receivedAccount = accountRepository.findByAccountNumAndAccountStatus(depositTransactionRequestDTO.getDepositWithdrawAccount(), AccountStatus.USED)  // 상대방 계좌
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_ACCOUNT));

    receivedAccount.setAccountAmount(receivedAccount.getAccountAmount() + depositTransactionRequestDTO.getTransactionAmount());
    accountRepository.save(receivedAccount);

    Transaction save = transactionRepository.save(
        Transaction.builder()
            .account(account)
            .transactionType(TransactionType.DEPOSIT)
            .transactionAmount(depositTransactionRequestDTO.getTransactionAmount())
            .depositWithdrawName(account.getAccountUser().getName())
            .receivedName(receivedAccount.getAccountUser().getName())
            .depositWithdrawAccount(depositTransactionRequestDTO.getDepositWithdrawAccount())
            .transactedAt(LocalDateTime.now())
            .build()
    );


    return DepositTransactionResponseDTO.toEntity(save);
  }

  public WithdrawTransactionResponseDTO withdrawTransaction(WithdrawTransactionRequestDTO withdrawTransactionRequestDTO, TokenAccountUserInfo accountUserInfo) {
    AccountUser accountUser = accountUserRepository.findById(accountUserInfo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_USER));
    String encodedPassword = accountUser.getPassword();
    if (!encoder.matches(withdrawTransactionRequestDTO.getPassword(), encodedPassword)) {
      throw new CustomException(ErrorCode.NOT_ACCORD_USER_PASSWORD);
    }
    Account account = accountRepository.findByAccountIdAndAccountStatus(withdrawTransactionRequestDTO.getAccountId(), AccountStatus.USED)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_ACCOUNT));
    if (account.getAccountAmount() == 0 || account.getAccountAmount() < withdrawTransactionRequestDTO.getTransactionAmount()) { // 거래 잔액이 부족할때
      throw new CustomException(ErrorCode.NOT_ENOUGH_AMOUNT);
    }
    account.setAccountAmount(account.getAccountAmount() - withdrawTransactionRequestDTO.getTransactionAmount());
    Transaction save = transactionRepository.save(
        Transaction.builder()
            .account(account)
            .transactionType(TransactionType.WITHDRAW)
            .transactionAmount(withdrawTransactionRequestDTO.getTransactionAmount())
            .depositWithdrawName(account.getAccountUser().getName())
            .receivedName(account.getAccountUser().getName())
            .depositWithdrawAccount(account.getAccountNum())
            .transactedAt(LocalDateTime.now())
            .build()
    );
    return WithdrawTransactionResponseDTO.toEntity(save);
  }

  public TransactionListResponseDTO getTransaction(TokenAccountUserInfo accountUserInfo, Long accountId, PageDTO pageDTO) {
    PageRequest pageable = PageRequest.of(
        pageDTO.getPage() - 1,
        pageDTO.getSize(),
        Sort.by("transactedAt").descending()
    );  // 최신순으로 조회

    log.info("pageable @@@@@@- {}", pageable);
    accountUserRepository.findById(accountUserInfo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_USER));
    Account account = accountRepository.findByAccountIdAndAccountStatus(accountId, AccountStatus.USED)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXISTS_ACCOUNT));
    Page<Transaction> byAccountId = transactionRepository.findByAccount(account, pageable);
    List<Transaction> transactionList = byAccountId.getContent();
    List<TransactionResponseDTO> detailList = transactionList.stream()
        .map(TransactionResponseDTO::new)
        .collect(Collectors.toList());
    log.info("byAccountId ---@@@@@@@ {}", byAccountId);
    log.info("transactionList ---@@@@@@@ {}", transactionList);
    log.info("detailList ---@@@@@@@ {}", detailList);
    TransactionListResponseDTO build = TransactionListResponseDTO.builder()
        .count(transactionList.size())
        .pageInfo(new PageResponseDTO(byAccountId))
        .transactions(detailList)
        .build();
    return build;
  }
}
