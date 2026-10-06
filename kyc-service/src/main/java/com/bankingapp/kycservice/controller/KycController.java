package com.bankingapp.kycservice.controller;

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

import com.bankingapp.kycservice.dto.KycResponse;
import com.bankingapp.kycservice.dto.ReviewKycRequest;
import com.bankingapp.kycservice.dto.SubmitKycRequest;
import com.bankingapp.kycservice.security.AuthenticatedUser;
import com.bankingapp.kycservice.service.KycService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
public class KycController {

    private final KycService kycService;

    @PostMapping("/submit")
    public ResponseEntity<KycResponse> submit(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody SubmitKycRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(kycService.submitKyc(user.userId(), request));
    }

    @GetMapping("/me")
    public ResponseEntity<KycResponse> myKyc(@AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(kycService.getMyKyc(user.userId()));
    }

    @PutMapping("/{id}/review")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<KycResponse> review(
            @PathVariable Long id,
            @Valid @RequestBody ReviewKycRequest request) {

        return ResponseEntity.ok(kycService.reviewKyc(id, request));
    }
}