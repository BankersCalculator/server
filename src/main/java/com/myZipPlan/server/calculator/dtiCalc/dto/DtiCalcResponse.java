package com.myZipPlan.server.calculator.dtiCalc.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;


@Getter
@Builder
public class DtiCalcResponse {

    private BigDecimal dtiRatio;
    private BigDecimal annualIncome;
    private BigDecimal annualRepaymentAmount; // 연간 원리금 상환액
    private BigDecimal annualRepaymentPrincipal; // 연간 원금 상환액
    private BigDecimal annualRepaymentInterest; // 연간 이자 상환액
    private BigDecimal yearlyLoanInterestRepayment; // 보유대출 연이자 상환액
}