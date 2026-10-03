package com.bankingapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransferRequest {

    @NotNull(message = "Sender account id is required")
    private Long fromAccountId;

    @NotNull(message = "Receiver account id is required")
    private Long toAccountId;

    @NotNull @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;
}