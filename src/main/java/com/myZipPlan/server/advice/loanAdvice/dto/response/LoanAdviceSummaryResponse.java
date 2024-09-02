package com.myZipPlan.server.advice.loanAdvice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class LoanAdviceSummaryResponse {

    private Long loanAdviceResultId;
    private String loanProductName;
    private String loanProductCode;
    private BigDecimal possibleLoanLimit;
    private BigDecimal expectedLoanRate;
}
