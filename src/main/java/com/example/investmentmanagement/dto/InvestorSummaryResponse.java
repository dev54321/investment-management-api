package com.example.investmentmanagement.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestorSummaryResponse {
    private Long investorId;
    private String investorName;
    private BigDecimal netAmount;
}
