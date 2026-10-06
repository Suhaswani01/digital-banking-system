package com.bankingapp.kycservice.dto;

import com.bankingapp.kycservice.entity.KycStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewKycRequest {

    @NotNull(message = "Status is required")
    private KycStatus status; // VERIFIED or REJECTED

    private String remarks;
}