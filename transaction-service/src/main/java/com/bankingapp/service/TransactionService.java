package com.bankingapp.service;

import com.bankingapp.client.AccountClient;
import com.bankingapp.client.AccountInfo;
import com.bankingapp.client.BalanceUpdateRequest;
import com.bankingapp.dto.*;
import com.bankingapp.entity.Transaction;
import com.bankingapp.entity.TransactionStatus;
import com.bankingapp.entity.TransactionType;
import com.bankingapp.exception.*;
import com.bankingapp.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;

    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        AccountInfo account = getActiveAccountOrThrow(request.getAccountId());

        accountClient.credit(account.id(), new BalanceUpdateRequest(request.getAmount()));

        BigDecimal newBalance = account.balance().add(request.getAmount());

        Transaction txn = saveTransaction(account.id(), TransactionType.DEPOSIT,
                request.getAmount(), newBalance, false, UUID.randomUUID().toString());

        return toResponse(txn);
    }

    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {
        AccountInfo account = getActiveAccountOrThrow(request.getAccountId());

        if (account.balance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in account: " + account.id());
        }

        accountClient.debit(account.id(), new BalanceUpdateRequest(request.getAmount()));

        BigDecimal newBalance = account.balance().subtract(request.getAmount());

        Transaction txn = saveTransaction(account.id(), TransactionType.WITHDRAW,
                request.getAmount(), newBalance, true, UUID.randomUUID().toString());

        return toResponse(txn);
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest request) {

        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new InvalidTransactionException("Sender and receiver account cannot be the same");
        }

        AccountInfo sender = getActiveAccountOrThrow(request.getFromAccountId());
        AccountInfo receiver = getActiveAccountOrThrow(request.getToAccountId());

        if (sender.balance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in account: " + sender.id());
        }

        String referenceId = UUID.randomUUID().toString();

        // Debit sender
        accountClient.debit(sender.id(), new BalanceUpdateRequest(request.getAmount()));
        BigDecimal senderNewBalance = sender.balance().subtract(request.getAmount());
        saveTransaction(sender.id(), TransactionType.TRANSFER, request.getAmount(),
                senderNewBalance, true, referenceId);

        // Credit receiver
        accountClient.credit(receiver.id(), new BalanceUpdateRequest(request.getAmount()));
        BigDecimal receiverNewBalance = receiver.balance().add(request.getAmount());
        Transaction creditTxn = saveTransaction(receiver.id(), TransactionType.TRANSFER, request.getAmount(),
                receiverNewBalance, false, referenceId);

        return toResponse(creditTxn);
    }

    public List<TransactionResponse> getHistory(Long accountId) {
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId).stream()
                .map(this::toResponse)
                .toList();
    }

    private AccountInfo getActiveAccountOrThrow(Long accountId) {
        AccountInfo account;
        try {
            account = accountClient.getAccount(accountId);
        } catch (Exception e) {
            throw new AccountNotFoundException("Account not found: " + accountId);
        }

        if (account == null) {
            throw new AccountNotFoundException("Account not found: " + accountId);
        }
        if (!"ACTIVE".equals(account.status())) {
            throw new AccountNotActiveException("Account is not active: " + accountId);
        }
        return account;
    }

    private Transaction saveTransaction(Long accountId, TransactionType type, BigDecimal amount,
                                         BigDecimal balanceAfter, boolean debit, String referenceId) {
        Transaction txn = Transaction.builder()
                .accountId(accountId)
                .type(type)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .debit(debit)
                .referenceId(debit ? referenceId : referenceId + "-CR")
                .status(TransactionStatus.SUCCESS)
                .build();
        return transactionRepository.save(txn);
    }

    private TransactionResponse toResponse(Transaction txn) {
        return TransactionResponse.builder()
                .id(txn.getId())
                .accountId(txn.getAccountId())
                .type(txn.getType())
                .amount(txn.getAmount())
                .balanceAfter(txn.getBalanceAfter())
                .referenceId(txn.getReferenceId())
                .status(txn.getStatus())
                .timestamp(txn.getTimestamp())
                .build();
    }
}