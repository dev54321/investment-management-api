package com.example.investmentmanagement.service;

import com.example.investmentmanagement.dto.FundRequest;
import com.example.investmentmanagement.dto.FundResponse;
import com.example.investmentmanagement.model.Fund;
import com.example.investmentmanagement.repository.FundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundService {

    private final FundRepository fundRepository;

    @Transactional
    public FundResponse create(FundRequest fundRequest) {
        Fund fund = toEntity(fundRequest);
        Fund saved = fundRepository.save(fund);
        return toResponse(saved);
    }

    public FundResponse findById(Long id){
        Fund fund =fundRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Fund not found"));
        return toResponse(fund);

    }

    public List<FundResponse> findAll(){
        return fundRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FundResponse update(Long id, FundRequest updatedFund){
        Fund existing = fundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fund not found"));
        existing.setName(updatedFund.getName());
        Fund saved = fundRepository.save(existing);
        return toResponse(saved);

    }

    @Transactional
    public void deleteById(Long id){
        fundRepository.deleteById(id);
    }


    private Fund toEntity(FundRequest request) {
        Fund fund = new Fund();
        fund.setName(request.getName());
        return fund;
    }

    private FundResponse toResponse(Fund fund) {
        FundResponse response = new FundResponse();
        response.setId(fund.getId());
        response.setName(fund.getName());
        return response;
    }


}
