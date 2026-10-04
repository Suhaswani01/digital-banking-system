package com.bankingapp.loanservice.service;

import com.bankingapp.loanservice.dto.*;
import com.bankingapp.loanservice.entity.*;
import com.bankingapp.loanservice.exception.InvalidLoanStateException;
import com.bankingapp.loanservice.exception.LoanNotFoundException;
import com.bankingapp.loanservice.repositories.EmiRepository;
import com.bankingapp.loanservice.repositories.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final EmiRepository emiRepository;
    private final EmiCalculator emiCalculator;

    public LoanResponse applyForLoan(Long userId, LoanApplicationRequest request) {
        Loan loan = Loan.builder()
                .userId(userId)
                .loanType(request.getLoanType())
                .principalAmount(request.getPrincipalAmount())
                .interestRate(request.getInterestRate())
                .tenureMonths(request.getTenureMonths())
                .status(LoanStatus.PENDING)
                .build();

        return toResponse(loanRepository.save(loan));
    }

    public List<LoanResponse> getMyLoans(Long userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public LoanResponse getLoanForUser(Long loanId, Long userId) {
        Loan loan = loanRepository.findByIdAndUserId(loanId, userId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));
        return toResponse(loan);
    }

    @Transactional
    public LoanResponse approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new InvalidLoanStateException("Only a PENDING loan can be approved");
        }

        var emiAmount = emiCalculator.calculateEmi(
                loan.getPrincipalAmount(), loan.getInterestRate(), loan.getTenureMonths());

        loan.setEmiAmount(emiAmount);
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setDecisionOn(java.time.LocalDateTime.now());
        loanRepository.save(loan);

        generateEmiSchedule(loan, emiAmount);

        return toResponse(loan);
    }

    public LoanResponse rejectLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new InvalidLoanStateException("Only a PENDING loan can be rejected");
        }

        loan.setStatus(LoanStatus.REJECTED);
        loan.setDecisionOn(java.time.LocalDateTime.now());
        return toResponse(loanRepository.save(loan));
    }

    public List<EmiResponse> getEmiSchedule(Long loanId) {
        return emiRepository.findByLoanIdOrderByInstallmentNumberAsc(loanId).stream()
                .map(this::toEmiResponse)
                .toList();
    }

    @Transactional
    public EmiResponse payNextEmi(Long loanId, Long userId) {

        Loan loan = loanRepository.findByIdAndUserId(loanId, userId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        List<Emi> schedule = emiRepository.findByLoanIdOrderByInstallmentNumberAsc(loanId);

        Emi nextDue = schedule.stream()
                .filter(e -> e.getStatus() == EmiStatus.PENDING)
                .findFirst()
                .orElseThrow(() -> new InvalidLoanStateException("No pending EMI for this loan"));

        nextDue.setStatus(EmiStatus.PAID);
        nextDue.setPaidOn(LocalDate.now());
        emiRepository.save(nextDue);

        boolean allPaid = schedule.stream()
                .allMatch(e -> e == nextDue || e.getStatus() == EmiStatus.PAID);

        if (allPaid) {
            loan.setStatus(LoanStatus.CLOSED);
            loanRepository.save(loan);
        }

        return toEmiResponse(nextDue);
    }

    private void generateEmiSchedule(Loan loan, java.math.BigDecimal emiAmount) {
        for (int i = 1; i <= loan.getTenureMonths(); i++) {
            Emi emi = Emi.builder()
                    .loanId(loan.getId())
                    .installmentNumber(i)
                    .dueDate(LocalDate.now().plusMonths(i))
                    .amount(emiAmount)
                    .status(EmiStatus.PENDING)
                    .build();
            emiRepository.save(emi);
        }
    }

    private LoanResponse toResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .loanType(loan.getLoanType())
                .principalAmount(loan.getPrincipalAmount())
                .interestRate(loan.getInterestRate())
                .tenureMonths(loan.getTenureMonths())
                .emiAmount(loan.getEmiAmount())
                .status(loan.getStatus())
                .appliedOn(loan.getAppliedOn())
                .decisionOn(loan.getDecisionOn())
                .build();
    }

    private EmiResponse toEmiResponse(Emi emi) {
        return EmiResponse.builder()
                .id(emi.getId())
                .installmentNumber(emi.getInstallmentNumber())
                .dueDate(emi.getDueDate())
                .amount(emi.getAmount())
                .status(emi.getStatus())
                .paidOn(emi.getPaidOn())
                .build();
    }
}