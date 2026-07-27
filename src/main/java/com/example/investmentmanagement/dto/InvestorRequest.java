package com.example.investmentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvestorRequest {
    @NotBlank(message = "Investor name is required")
    private String name;
}
