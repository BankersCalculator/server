package com.bankersCalculator.server.advice.loanAdvice.dto.internal;

import com.bankersCalculator.server.common.enums.JeonseLoanProductType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class OptimalLoanProductResult {

    private JeonseLoanProductType productType;
    private BigDecimal possibleLoanLimit;
    private BigDecimal expectedLoanRate;

}
