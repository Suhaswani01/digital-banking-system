package com.bankingapp.beneficiary.service;



import java.util.List;

import org.springframework.stereotype.Service;

import com.bankingapp.beneficiary.dto.AddBeneficiaryRequest;
import com.bankingapp.beneficiary.dto.BeneficiaryResponse;
import com.bankingapp.beneficiary.entity.Beneficiary;
import com.bankingapp.beneficiary.exception.BeneficiaryNotFoundException;
import com.bankingapp.beneficiary.exception.DuplicateBeneficiaryException;
import com.bankingapp.beneficiary.repositories.BeneficiaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryResponse addBeneficiary(Long userId, AddBeneficiaryRequest request) {

        if (beneficiaryRepository.existsByUserIdAndBeneficiaryAccountNumber(
                userId, request.getBeneficiaryAccountNumber())) {
            throw new DuplicateBeneficiaryException("This beneficiary is already added");
        }

        Beneficiary beneficiary = Beneficiary.builder()
                .userId(userId)
                .beneficiaryAccountNumber(request.getBeneficiaryAccountNumber())
                .beneficiaryName(request.getBeneficiaryName())
                .bankName(request.getBankName())
                .build();

        return toResponse(beneficiaryRepository.save(beneficiary));
    }

    public List<BeneficiaryResponse> getMyBeneficiaries(Long userId) {
        return beneficiaryRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteBeneficiary(Long id, Long userId) {
        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BeneficiaryNotFoundException("Beneficiary not found"));
        beneficiaryRepository.delete(beneficiary);
    }

    private BeneficiaryResponse toResponse(Beneficiary b) {
        return BeneficiaryResponse.builder()
                .id(b.getId())
                .beneficiaryAccountNumber(b.getBeneficiaryAccountNumber())
                .beneficiaryName(b.getBeneficiaryName())
                .bankName(b.getBankName())
                .addedOn(b.getAddedOn())
                .build();
    }
}
