package com.myZipPlan.server.advice.loanAdvice.dto.internal;

import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@ToString
public class BestLoanProductResult {

    private JeonseLoanProductType productType;
    private BigDecimal possibleLoanLimit;
    private BigDecimal expectedLoanRate;

}
