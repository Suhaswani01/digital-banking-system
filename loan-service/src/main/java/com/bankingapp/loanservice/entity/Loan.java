package com.bankingapp.loanservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String loanType;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal principalAmount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate; // annual rate, e.g. 10.00 means 10%

    @Column(nullable = false)
    private Integer tenureMonths;

    @Column(precision = 19, scale = 2)
    private BigDecimal emiAmount; // calculated only after approval

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    @Column(updatable = false)
    private LocalDateTime appliedOn;

    private LocalDateTime decisionOn; // when approved/rejected

    @PrePersist
    protected void onCreate() {
        appliedOn = LocalDateTime.now();
        if (status == null) status = LoanStatus.PENDING;
    }
}