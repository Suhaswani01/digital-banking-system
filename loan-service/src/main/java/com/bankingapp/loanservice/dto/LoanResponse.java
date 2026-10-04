package com.bankingapp.loanservice.dto;

import com.bankingapp.loanservice.entity.LoanStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoanResponse {
    private Long id;
    private String loanType;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal emiAmount;
    private LoanStatus status;
    private LocalDateTime appliedOn;
    private LocalDateTime decisionOn;
}