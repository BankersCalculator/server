package com.bankersCalculator.server.advice.loanAdvice.dto.api;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class RecommendedProductDto {
    private Integer rank;
    private String loanProductName;
    private String loanProductCode;
    private BigDecimal possibleLoanLimit;
    private BigDecimal expectedLoanRate;
    private List<String> notEligibleReasons;
}