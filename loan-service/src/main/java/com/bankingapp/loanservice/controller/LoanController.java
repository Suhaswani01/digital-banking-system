package com.bankingapp.loanservice.controller;

import com.bankingapp.loanservice.dto.*;
import com.bankingapp.loanservice.security.AuthenticatedUser;
import com.bankingapp.loanservice.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@EnableMethodSecurity
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/apply")
    public ResponseEntity<LoanResponse> apply(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody LoanApplicationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loanService.applyForLoan(user.userId(), request));
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> myLoans(@AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(loanService.getMyLoans(user.userId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoan(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id) {

        return ResponseEntity.ok(loanService.getLoanForUser(id, user.userId()));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<LoanResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.approveLoan(id));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<LoanResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.rejectLoan(id));
    }

    @GetMapping("/{id}/emi-schedule")
    public ResponseEntity<List<EmiResponse>> emiSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getEmiSchedule(id));
    }

    
    
    @PostMapping("/{id}/pay-emi")
    public ResponseEntity<EmiResponse> payEmi(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id) {

        return ResponseEntity.ok(loanService.payNextEmi(id, user.userId()));
    }
}