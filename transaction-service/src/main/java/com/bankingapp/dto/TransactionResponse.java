package com.bankingapp.dto;

import com.bankingapp.entity.TransactionStatus;
import com.bankingapp.entity.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionResponse {
    private Long id;
    private Long accountId;
    private TransactionType type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String referenceId;
    private TransactionStatus status;
    private LocalDateTime timestamp;
}