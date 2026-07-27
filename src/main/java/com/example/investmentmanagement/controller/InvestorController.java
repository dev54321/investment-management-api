package com.example.investmentmanagement.controller;



import com.example.investmentmanagement.dto.InvestorRequest;
import com.example.investmentmanagement.dto.InvestorResponse;
import com.example.investmentmanagement.service.InvestorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investor")
@RequiredArgsConstructor
public class InvestorController {

    private final InvestorService investorService;

    @PostMapping
    public ResponseEntity<InvestorResponse> create(@Valid @RequestBody InvestorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(investorService.create(request));
    }

    @GetMapping
    public List<InvestorResponse> findAll() {
        return investorService.findAll();
    }

    @GetMapping("/{id}")
    public InvestorResponse findById(@PathVariable Long id) {
        return investorService.findById(id);
    }

    @PutMapping("/{id}")
    public InvestorResponse update(@PathVariable Long id, @Valid @RequestBody InvestorRequest request) {
        return investorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        investorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}