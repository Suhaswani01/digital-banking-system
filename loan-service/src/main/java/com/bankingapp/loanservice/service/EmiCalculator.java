package com.bankingapp.loanservice.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class EmiCalculator {

    
    public BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualRatePercent, int tenureMonths) {

        // Monthly interest rate as a decimal, e.g. 10% annual -> 0.0083333...
        BigDecimal monthlyRate = annualRatePercent
                .divide(BigDecimal.valueOf(12), MathContext.DECIMAL64)
                .divide(BigDecimal.valueOf(100), MathContext.DECIMAL64);

        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowN = onePlusR.pow(tenureMonths, MathContext.DECIMAL64);

        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRPowN);
        BigDecimal denominator = onePlusRPowN.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}