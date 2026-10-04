package com.bankingapp.beneficiary.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BeneficiaryResponse {
    private Long id;
    private String beneficiaryAccountNumber;
    private String beneficiaryName;
    private String bankName;
    private LocalDateTime addedOn;
}