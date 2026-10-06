package com.bankingapp.kycservice.repositories;

import com.bankingapp.kycservice.entity.KycRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KycRepository extends JpaRepository<KycRecord, Long> {
    Optional<KycRecord> findByUserId(Long userId);
}