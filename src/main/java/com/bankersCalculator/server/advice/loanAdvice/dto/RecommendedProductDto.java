package com.bankersCalculator.server.advice.loanAdvice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendedProductDto {
    private int rank;
    private String loanProductName;
    private String loanProductCode;
    private double possibleLoanLimit;
    private double expectedLoanRate;
    private String notEligibleReason;
}