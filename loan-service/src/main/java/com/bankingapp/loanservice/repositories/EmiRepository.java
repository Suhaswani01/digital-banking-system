package com.bankingapp.loanservice.repositories;

import com.bankingapp.loanservice.entity.Emi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmiRepository extends JpaRepository<Emi, Long> {
    List<Emi> findByLoanIdOrderByInstallmentNumberAsc(Long loanId);
}
