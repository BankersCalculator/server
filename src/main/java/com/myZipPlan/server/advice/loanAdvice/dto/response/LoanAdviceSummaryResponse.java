package com.myZipPlan.server.advice.loanAdvice.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoanAdviceSummaryResponse {

    private Long loanAdviceResultId;
    private String loanProductName;
    private String loanProductCode;
    private Long possibleLoanLimit;
    private Double expectedLoanRate;
}
