package com.bankingapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DepositRequest {

    @NotNull(message = "Account id is required")
    private Long accountId;

    @NotNull @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;
}