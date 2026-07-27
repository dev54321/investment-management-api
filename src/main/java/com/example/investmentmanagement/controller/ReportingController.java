package com.example.investmentmanagement.controller;

import com.example.investmentmanagement.dto.FundBalanceResponse;
import com.example.investmentmanagement.dto.InvestorSummaryResponse;
import com.example.investmentmanagement.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping("/funds/{id}/balance")
    public FundBalanceResponse fundBalance(@PathVariable Long id) {
        return reportingService.getFundBalance(id);
    }

    @GetMapping("/investors/{id}/summary")
    public InvestorSummaryResponse investorSummary(@PathVariable Long id) {
        return reportingService.getInvestorSummary(id);
    }
}
