package com.example.investmentmanagement.controller;

import com.example.investmentmanagement.dto.FundRequest;
import com.example.investmentmanagement.dto.FundResponse;
import com.example.investmentmanagement.service.FundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funds")
@RequiredArgsConstructor
public class FundController {

    private final FundService fundService;

    @PostMapping
    public ResponseEntity<FundResponse> create(@Valid @RequestBody FundRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fundService.create(request));
    }

    @GetMapping
    public List<FundResponse> findAll() {
        return fundService.findAll();
    }

    @GetMapping("/{id}")
    public FundResponse findById(@PathVariable Long id) {
        return fundService.findById(id);
    }

    @PutMapping("/{id}")
    public FundResponse update(@PathVariable Long id, @Valid @RequestBody FundRequest request) {
        return fundService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        fundService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
