package com.example.investmentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FundBalanceResponse {
    private Long fundId;
    private String fundName;
    private BigDecimal balance;
}
