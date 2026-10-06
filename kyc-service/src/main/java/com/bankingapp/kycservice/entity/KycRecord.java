package com.bankingapp.kycservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "kyc_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId; // one KYC record per user

    @Column(nullable = false)
    private String documentType;   // e.g. AADHAAR, PAN, PASSPORT

    @Column(nullable = false)
    private String documentNumber; // metadata only, no file upload

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KycStatus status;

    private String remarks; // reason, filled in when rejected

    @Column(updatable = false)
    private LocalDateTime submittedOn;

    private LocalDateTime reviewedOn;

    @PrePersist
    protected void onCreate() {
        submittedOn = LocalDateTime.now();
        if (status == null) status = KycStatus.PENDING;
    }
}