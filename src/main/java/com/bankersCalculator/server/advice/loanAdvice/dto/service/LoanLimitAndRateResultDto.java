package com.bankersCalculator.server.advice.loanAdvice.dto.service;

import com.bankersCalculator.server.common.enums.JeonseLoanProductType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class LoanLimitAndRateResultDto {

    private JeonseLoanProductType productType;
    private BigDecimal possibleLoanLimit;
    private BigDecimal expectedLoanRate;
}
