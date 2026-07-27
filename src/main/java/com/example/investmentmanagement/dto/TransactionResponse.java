package com.example.investmentmanagement.dto;

import com.example.investmentmanagement.model.TransactionEffect;
import com.example.investmentmanagement.model.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TransactionResponse {
    private Long id;
    private Long fundId;
    private Long investorId;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionEffect effect;
}
