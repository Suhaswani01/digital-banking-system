package com.bankingapp.beneficiary.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankingapp.beneficiary.dto.AddBeneficiaryRequest;
import com.bankingapp.beneficiary.dto.BeneficiaryResponse;
import com.bankingapp.beneficiary.service.BeneficiaryService;
import com.bankingapp.security.AuthenticatedUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping
    public ResponseEntity<BeneficiaryResponse> add(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody AddBeneficiaryRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(beneficiaryService.addBeneficiary(user.userId(), request));
    }

    @GetMapping
    public ResponseEntity<List<BeneficiaryResponse>> getMine(
            @AuthenticationPrincipal AuthenticatedUser user) {

        return ResponseEntity.ok(beneficiaryService.getMyBeneficiaries(user.userId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id) {

        beneficiaryService.deleteBeneficiary(id, user.userId());
        return ResponseEntity.noContent().build();
    }
}
