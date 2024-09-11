package com.myZipPlan.server.advice.loanAdvice.dto.response;

import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
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


    public static LoanAdviceSummaryResponse fromEntity(LoanAdviceResult loanAdviceResult) {
        return LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(loanAdviceResult.getId())
                .loanProductName(loanAdviceResult.getLoanProductName())
                .loanProductCode(loanAdviceResult.getLoanProductCode())
                .possibleLoanLimit(loanAdviceResult.getPossibleLoanLimit())
                .expectedLoanRate(loanAdviceResult.getExpectedLoanRate())
                .build();
    }
}
