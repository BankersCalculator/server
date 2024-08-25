package com.bankersCalculator.server.advice.loanAdvice.dto.service;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class OptimalLoanProductResult {

    private String LoanProductCode;
    private BigDecimal possibleLoanLimit;
    private BigDecimal expectedLoanRate;

}
