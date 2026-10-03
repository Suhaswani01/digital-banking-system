package com.bankingapp.client;

import java.math.BigDecimal;

public record AccountInfo(
        Long id,
        String accountNumber,
        String accountType,
        BigDecimal balance,
        String status
) {}