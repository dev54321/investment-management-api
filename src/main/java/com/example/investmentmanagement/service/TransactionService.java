package com.example.investmentmanagement.service;

import com.example.investmentmanagement.dto.TransactionRequest;
import com.example.investmentmanagement.dto.TransactionResponse;
import com.example.investmentmanagement.model.Fund;
import com.example.investmentmanagement.model.Investor;
import com.example.investmentmanagement.model.Transaction;
import com.example.investmentmanagement.repository.FundRepository;
import com.example.investmentmanagement.repository.InvestorRepository;
import com.example.investmentmanagement.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final FundRepository fundRepository;
    private final InvestorRepository investorRepository;

    @Transactional
    public TransactionResponse create(TransactionRequest transaction){
        Transaction entity = toEntity(transaction);
        Transaction saved = transactionRepository.save(entity);
        return toResponse(saved);
    }

    public TransactionResponse findById(Long id){
        Transaction transaction = transactionRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return toResponse(transaction);
    }

    public List<TransactionResponse> findAll(){
        return transactionRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public void deleteById(Long id){
        transactionRepository.deleteById(id);
    }

    private Transaction toEntity(TransactionRequest request){
        Transaction transaction = new Transaction();
        Fund fund = fundRepository.findById(request.getFundId())
                .orElseThrow(() -> new RuntimeException("Fund not found"));
        Investor investor =investorRepository.findById(request.getInvestorId())
                .orElseThrow(() -> new RuntimeException("Investor not found"));


        transaction.setFund(fund);
        transaction.setInvestor(investor);
        transaction.setType(request.getType());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setAmount(request.getAmount());
        return transaction;
    }

    private TransactionResponse toResponse(Transaction transaction){
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setTransactionDate(transaction.getTransactionDate());
        response.setFundId(transaction.getFund().getId());
        response.setInvestorId(transaction.getInvestor().getId());
        response.setEffect(transaction.getType().getEffect());
        return response;
    }
}
