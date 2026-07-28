package com.example.investmentmanagement.service;

import com.example.investmentmanagement.dto.InvestorRequest;
import com.example.investmentmanagement.dto.InvestorResponse;
import com.example.investmentmanagement.exception.ResourceNotFoundException;
import com.example.investmentmanagement.model.Investor;
import com.example.investmentmanagement.repository.InvestorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestorService {

    private final InvestorRepository investorRepository;

    @Transactional
    public InvestorResponse create(InvestorRequest investorRequest){
        Investor investor = toEntity(investorRequest);
        Investor saved = investorRepository.save(investor);
        return toResponse(saved);
    }

    public InvestorResponse findById(Long id){
        Investor investor = investorRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Investor not found"));
        return toResponse(investor);
    }

    public List<InvestorResponse> findAll() {
        return  investorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public InvestorResponse update(Long id, InvestorRequest updatedInvestor){
        Investor existing = investorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found"));
        existing.setName(updatedInvestor.getName());
        Investor saved = investorRepository.save(existing);
        return toResponse(saved);
    }

    @Transactional
    public void deleteById(Long id){
        investorRepository.deleteById(id);
    }

    private Investor toEntity(InvestorRequest request){
        Investor investor = new Investor();
        investor.setName(request.getName());
        return  investor;
    }

    private InvestorResponse toResponse(Investor investor){
        InvestorResponse investorResponse = new InvestorResponse();
        investorResponse.setId(investor.getId());
        investorResponse.setName(investor.getName());
        return investorResponse;
    }
}
