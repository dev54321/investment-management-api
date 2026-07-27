package com.example.investmentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FundRequest {
    @NotBlank(message = "Fund name is required")
    private String name;
}
