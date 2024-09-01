package com.myZipPlan.server.advice.loanAdvice.dto.internal;

import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class LoanLimitAndRateResultDto {

    private JeonseLoanProductType productType;
    private boolean isEligible;
    private BigDecimal possibleLoanLimit;
    private BigDecimal expectedLoanRate;

    public void setIsEligible(boolean isEligible) {
        this.isEligible = isEligible;
    }
}
