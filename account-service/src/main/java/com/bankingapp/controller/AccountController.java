package com.bankingapp.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankingapp.dto.AccountResponse;
import com.bankingapp.dto.BalanceUpdateRequest;
import com.bankingapp.dto.CreateAccountRequest;
import com.bankingapp.dto.UpdateStatusRequest;
import com.bankingapp.security.AuthenticatedUser;
import com.bankingapp.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody CreateAccountRequest request) {

        AccountResponse response = accountService.createAccount(user.userId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getMyAccounts(
            @AuthenticationPrincipal AuthenticatedUser user) {

        return ResponseEntity.ok(accountService.getMyAccounts(user.userId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id) {

        return ResponseEntity.ok(accountService.getAccountForUser(id, user.userId()));
    }
    
   

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id) {

        return ResponseEntity.ok(accountService.getBalance(id, user.userId()));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<AccountResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {

        return ResponseEntity.ok(accountService.updateStatus(id, request.getStatus()));
    }
    @PostMapping("/internal/{id}/debit")
    public ResponseEntity<Void> debitAccount(
            @PathVariable Long id,
            @Valid @RequestBody BalanceUpdateRequest request) {

        accountService.debit(id, request.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/internal/{id}/credit")
    public ResponseEntity<Void> creditAccount(
            @PathVariable Long id,
            @Valid @RequestBody BalanceUpdateRequest request) {

        accountService.credit(id, request.getAmount());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/internal/{id}")
    public ResponseEntity<AccountResponse> getAccountInternal(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }
}
