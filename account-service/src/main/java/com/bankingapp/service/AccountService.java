package com.bankingapp.service;

import com.bankingapp.dto.AccountResponse;
import com.bankingapp.dto.CreateAccountRequest;
import com.bankingapp.entity.Account;
import com.bankingapp.entity.AccountStatus;
import com.bankingapp.exception.AccountNotFoundException;
import com.bankingapp.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private static final SecureRandom RANDOM = new SecureRandom();

    public AccountResponse createAccount(Long userId, CreateAccountRequest request) {

        Account account = Account.builder()
                .userId(userId)
                .accountNumber(generateUniqueAccountNumber())
                .accountType(request.getAccountType())
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .build();

        Account saved = accountRepository.save(account);
        return toResponse(saved);
    }

    public List<AccountResponse> getMyAccounts(Long userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public AccountResponse getAccountForUser(Long accountId, Long userId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (!account.getUserId().equals(userId)) {
            // Don't say "forbidden" - that confirms the account exists.
            // Treat it the same as not found.
            throw new AccountNotFoundException("Account not found");
        }

        return toResponse(account);
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = "AC" + (100000000 + RANDOM.nextInt(900000000));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    private AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .build();
    }
}