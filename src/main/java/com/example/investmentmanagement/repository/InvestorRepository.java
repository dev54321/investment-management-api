package com.example.investmentmanagement.repository;

import com.example.investmentmanagement.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorRepository extends JpaRepository<Investor, Long> { }