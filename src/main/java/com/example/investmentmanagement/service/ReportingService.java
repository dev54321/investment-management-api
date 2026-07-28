package com.example.investmentmanagement.service;

import com.example.investmentmanagement.dto.FundBalanceResponse;
import com.example.investmentmanagement.dto.InvestorSummaryResponse;
import com.example.investmentmanagement.exception.ResourceNotFoundException;
import com.example.investmentmanagement.model.Fund;
import com.example.investmentmanagement.model.Investor;
import com.example.investmentmanagement.model.Transaction;
import com.example.investmentmanagement.model.TransactionEffect;
import com.example.investmentmanagement.repository.FundRepository;
import com.example.investmentmanagement.repository.InvestorRepository;
import com.example.investmentmanagement.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final TransactionRepository transactionRepository;
    private final FundRepository fundRepository;
    private final InvestorRepository investorRepository;

    public FundBalanceResponse getFundBalance(Long fundId) {
        Fund fund = fundRepository.findById(fundId)
                .orElseThrow(() -> new ResourceNotFoundException("Fund not found"));
        BigDecimal balance = calculateNet(transactionRepository.findByFundId(fundId));
        return new FundBalanceResponse(fund.getId(), fund.getName(), balance);
    }

    public InvestorSummaryResponse getInvestorSummary(Long investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found"));
        BigDecimal net = calculateNet(transactionRepository.findByInvestorId(investorId));
        return new InvestorSummaryResponse(investor.getId(), investor.getName(), net);
    }

    private BigDecimal calculateNet(List<Transaction> transactions) {
        BigDecimal total = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            if (t.getType().getEffect() == TransactionEffect.CREDIT) {
                total = total.add(t.getAmount());
            } else {
                total = total.subtract(t.getAmount());
            }
        }
        return total;
    }
}