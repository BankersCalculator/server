package com.bankersCalculator.server.advice.loanAdvice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoanAdviceSummaryResponse {

    private final Long loanAdviceResultId;
    private String loanProductName;
    private String loanProductCode;
    private double possibleLoanLimit;
    private double expectedLoanRate;
}
