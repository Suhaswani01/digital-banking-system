package com.bankingapp.beneficiary.repositories;

import com.bankingapp.beneficiary.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByUserId(Long userId);
    Optional<Beneficiary> findByIdAndUserId(Long id, Long userId);
    boolean existsByUserIdAndBeneficiaryAccountNumber(Long userId, String accountNumber);
}