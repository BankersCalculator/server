package com.bankersCalculator.bankersCalculator.dsrCalc.dto;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DsrCalcRequest {

    // TODO: @Valid 추가할 것.

    private List<LoanStatus> loanStatusList;

    private int annualIncome;

    @Getter
    @NoArgsConstructor
    public static class LoanStatus {
        private RepaymentType repaymentType;
        private LoanType loanType;
        private double principal;
        private int term;
        private int gracePeriod;
        private int remainingTerm;
        private double interestRate;
    }



}
