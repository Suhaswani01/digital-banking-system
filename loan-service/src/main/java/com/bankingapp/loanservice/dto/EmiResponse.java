package com.bankingapp.loanservice.dto;

import com.bankingapp.loanservice.entity.EmiStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmiResponse {
    private Long id;
    private Integer installmentNumber;
    private LocalDate dueDate;
    private BigDecimal amount;
    private EmiStatus status;
    private LocalDate paidOn;
}