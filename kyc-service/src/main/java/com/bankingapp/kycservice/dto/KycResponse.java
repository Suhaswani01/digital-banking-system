package com.bankingapp.kycservice.dto;

import com.bankingapp.kycservice.entity.KycStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KycResponse {
    private Long id;
    private Long userId;
    private String documentType;
    private String documentNumber;
    private KycStatus status;
    private String remarks;
    private LocalDateTime submittedOn;
    private LocalDateTime reviewedOn;
}