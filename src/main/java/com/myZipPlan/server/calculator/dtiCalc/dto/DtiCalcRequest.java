package com.myZipPlan.server.calculator.dtiCalc.dto;

import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class DtiCalcRequest {

    private BigDecimal annualIncome;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private Integer loanTerm;
    private RepaymentType repaymentType;
    private BigDecimal yearlyLoanInterestRepayment; // 보유대출 연이자 상환액


    public DtiCalcServiceRequest toServiceRequest() {
        return DtiCalcServiceRequest.builder()
            .annualIncome(annualIncome)
            .loanAmount(loanAmount)
            .interestRate(interestRate)
            .loanTerm(loanTerm)
            .repaymentType(repaymentType)
            .yearlyLoanInterestRepayment(yearlyLoanInterestRepayment)
            .build();
    }


}
