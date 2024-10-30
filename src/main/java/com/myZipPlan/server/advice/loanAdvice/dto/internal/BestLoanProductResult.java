package com.myZipPlan.server.advice.loanAdvice.dto.internal;

import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class BestLoanProductResult {

    private JeonseLoanProductType productType;
    private String productName;
    private BigDecimal possibleLoanLimit;
    private BigDecimal expectedLoanRate;


    @Builder
    public BestLoanProductResult(JeonseLoanProductType productType, BigDecimal possibleLoanLimit, BigDecimal expectedLoanRate) {
        this.productType = productType;
        this.productName = productType.getProductName();
        this.possibleLoanLimit = possibleLoanLimit;
        this.expectedLoanRate = expectedLoanRate;
    }
}
