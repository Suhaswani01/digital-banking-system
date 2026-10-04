package com.bankingapp.beneficiary.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "beneficiaries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 20)
    private String beneficiaryAccountNumber;

    @Column(nullable = false)
    private String beneficiaryName;

    @Column(nullable = false)
    private String bankName;

    @Column(updatable = false)
    private LocalDateTime addedOn;

    @PrePersist
    protected void onCreate() {
        addedOn = LocalDateTime.now();
    }
}