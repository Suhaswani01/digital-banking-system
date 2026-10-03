package com.bankingapp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service")
public interface AccountClient {

    @PostMapping("/api/accounts/internal/{id}/debit")
    ResponseEntity<Void> debit(@PathVariable Long id, @RequestBody BalanceUpdateRequest request);

    @PostMapping("/api/accounts/internal/{id}/credit")
    ResponseEntity<Void> credit(@PathVariable Long id, @RequestBody BalanceUpdateRequest request);

    @GetMapping("/api/accounts/internal/{id}")
    AccountInfo getAccount(@PathVariable Long id);
}
