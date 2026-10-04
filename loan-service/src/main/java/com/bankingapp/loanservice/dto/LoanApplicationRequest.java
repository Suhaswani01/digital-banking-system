package com.bankingapp.loanservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoanApplicationRequest {

    @NotBlank(message = "Loan type is required")
    private String loanType;

    @NotNull @Positive(message = "Principal amount must be greater than zero")
    private BigDecimal principalAmount;

    @NotNull @Positive(message = "Interest rate must be greater than zero")
    private BigDecimal interestRate;

    @NotNull @Min(value = 1, message = "Tenure must be at least 1 month")
    private Integer tenureMonths;
}