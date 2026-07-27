package com.example.investmentmanagement.repository;

import com.example.investmentmanagement.model.Fund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundRepository extends JpaRepository<Fund, Long> { }