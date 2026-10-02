package com.bankingapp.controller;

import com.bankingapp.dto.AccountResponse;
import com.bankingapp.dto.CreateAccountRequest;
import com.bankingapp.security.AuthenticatedUser;
import com.bankingapp.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}