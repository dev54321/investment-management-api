package com.example.investmentmanagement.repository;

import com.example.investmentmanagement.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFundId(Long fundId);
    List<Transaction> findByInvestorId(Long investorId);
}