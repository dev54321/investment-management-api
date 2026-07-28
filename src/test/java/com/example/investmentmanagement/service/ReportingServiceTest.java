package com.example.investmentmanagement.service;

import com.example.investmentmanagement.dto.FundBalanceResponse;
import com.example.investmentmanagement.exception.ResourceNotFoundException;
import com.example.investmentmanagement.model.*;
import com.example.investmentmanagement.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportingServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private FundRepository fundRepository;
    @Mock
    private InvestorRepository investorRepository;

    @InjectMocks
    private ReportingService reportingService;

    @Test
    void getFundBalance_appliesCreditsAndDebits() {
        // given: a fund and a known set of transactions
        Fund fund = new Fund();
        fund.setId(1L);
        fund.setName("Test Fund");

        List<Transaction> transactions = List.of(
                transaction(TransactionType.CONTRIBUTION, "100"),    // +100 credit
                transaction(TransactionType.INTEREST_INCOME, "50"),  // +50  credit
                transaction(TransactionType.MANAGEMENT_FEE, "30"),   // -30  debit
                transaction(TransactionType.DISTRIBUTION, "20")      // -20  debit
        );

        when(fundRepository.findById(1L)).thenReturn(Optional.of(fund));
        when(transactionRepository.findByFundId(1L)).thenReturn(transactions);

        // when
        FundBalanceResponse result = reportingService.getFundBalance(1L);

        // then: 100 + 50 - 30 - 20 = 100
        assertEquals(new BigDecimal("100"), result.getBalance());
    }

    @Test
    void getFundBalance_withOnlyDebits_isNegative() {
        Fund fund = new Fund();
        fund.setId(1L);
        fund.setName("Test Fund");

        List<Transaction> transactions = List.of(
                transaction(TransactionType.MANAGEMENT_FEE, "40"),
                transaction(TransactionType.DISTRIBUTION, "60")
        );

        when(fundRepository.findById(1L)).thenReturn(Optional.of(fund));
        when(transactionRepository.findByFundId(1L)).thenReturn(transactions);

        FundBalanceResponse result = reportingService.getFundBalance(1L);

        assertEquals(new BigDecimal("-100"), result.getBalance());
    }

    @Test
    void getFundBalance_fundNotFound_throws() {
        when(fundRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> reportingService.getFundBalance(999L));
    }

    private Transaction transaction(TransactionType type, String amount) {
        Transaction t = new Transaction();
        t.setType(type);
        t.setAmount(new BigDecimal(amount));
        t.setTransactionDate(LocalDate.now());
        return t;
    }
}
