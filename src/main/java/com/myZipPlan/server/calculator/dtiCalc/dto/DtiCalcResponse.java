package com.myZipPlan.server.calculator.dtiCalc.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Getter
@Builder
public class DtiCalcResponse {

    private BigDecimal dtiRatio;
    private BigDecimal annualIncome;
    private BigDecimal annualRepaymentAmount; // 연간 원리금 상환액
    private BigDecimal annualRepaymentPrincipal; // 연간 원금 상환액
    private BigDecimal annualRepaymentInterest; // 연간 이자 상환액
    private BigDecimal yearlyLoanInterestRepayment; // 보유대출 연이자 상환액

    @Builder
    public DtiCalcResponse(BigDecimal dtiRatio,
                           BigDecimal annualIncome,
                           BigDecimal annualRepaymentAmount,
                           BigDecimal annualRepaymentPrincipal,
                           BigDecimal annualRepaymentInterest,
                           BigDecimal yearlyLoanInterestRepayment) {
        this.dtiRatio = dtiRatio.setScale(4, RoundingMode.HALF_UP);
        this.annualIncome = annualIncome.setScale(0, RoundingMode.HALF_UP);
        this.annualRepaymentAmount = annualRepaymentAmount.setScale(0, RoundingMode.HALF_UP);
        this.annualRepaymentPrincipal = annualRepaymentPrincipal.setScale(0, RoundingMode.HALF_UP);
        this.annualRepaymentInterest = annualRepaymentInterest.setScale(0, RoundingMode.HALF_UP);
        this.yearlyLoanInterestRepayment = yearlyLoanInterestRepayment.setScale(0, RoundingMode.HALF_UP);
    }
}