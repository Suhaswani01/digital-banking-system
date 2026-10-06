package com.bankingapp.kycservice.service;

import com.bankingapp.kycservice.dto.*;
import com.bankingapp.kycservice.entity.KycRecord;
import com.bankingapp.kycservice.entity.KycStatus;
import com.bankingapp.kycservice.exception.InvalidKycStateException;
import com.bankingapp.kycservice.exception.KycNotFoundException;
import com.bankingapp.kycservice.repositories.KycRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KycService {

    private final KycRepository kycRepository;

    public KycResponse submitKyc(Long userId, SubmitKycRequest request) {

        KycRecord record = kycRepository.findByUserId(userId).orElse(null);

        if (record != null) {
            if (record.getStatus() == KycStatus.VERIFIED) {
                throw new InvalidKycStateException("KYC is already verified and cannot be resubmitted");
            }
            // Resubmitting after a rejection, or updating a pending submission
            record.setDocumentType(request.getDocumentType());
            record.setDocumentNumber(request.getDocumentNumber());
            record.setStatus(KycStatus.PENDING);
            record.setRemarks(null);
            record.setReviewedOn(null);
        } else {
            record = KycRecord.builder()
                    .userId(userId)
                    .documentType(request.getDocumentType())
                    .documentNumber(request.getDocumentNumber())
                    .status(KycStatus.PENDING)
                    .build();
        }

        return toResponse(kycRepository.save(record));
    }

    public KycResponse getMyKyc(Long userId) {
        KycRecord record = kycRepository.findByUserId(userId)
                .orElseThrow(() -> new KycNotFoundException("No KYC record found"));
        return toResponse(record);
    }

    public KycResponse reviewKyc(Long kycId, ReviewKycRequest request) {

        if (request.getStatus() == KycStatus.PENDING) {
            throw new InvalidKycStateException("Review must set status to VERIFIED or REJECTED");
        }

        KycRecord record = kycRepository.findById(kycId)
                .orElseThrow(() -> new KycNotFoundException("KYC record not found"));

        if (record.getStatus() != KycStatus.PENDING) {
            throw new InvalidKycStateException("Only a PENDING KYC record can be reviewed");
        }

        record.setStatus(request.getStatus());
        record.setRemarks(request.getRemarks());
        record.setReviewedOn(LocalDateTime.now());

        return toResponse(kycRepository.save(record));
    }

    private KycResponse toResponse(KycRecord record) {
        return KycResponse.builder()
                .id(record.getId())
                .userId(record.getUserId())
                .documentType(record.getDocumentType())
                .documentNumber(record.getDocumentNumber())
                .status(record.getStatus())
                .remarks(record.getRemarks())
                .submittedOn(record.getSubmittedOn())
                .reviewedOn(record.getReviewedOn())
                .build();
    }
}