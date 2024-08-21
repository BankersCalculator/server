package com.bankersCalculator.server.advice.loanAdvice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RecommendedProductDto {
    private Integer rank;
    private String loanProductName;
    private String loanProductCode;
    private Long possibleLoanLimit;
    private Double expectedLoanRate;
    private List<String> notEligibleReasons;
}