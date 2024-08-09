package com.bankersCalculator.server.calculator.dsrCalc.dto;

import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcServiceRequest;
import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.common.enums.RepaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DsrCalcServiceRequest {


    private List<LoanStatus> loanStatusList;

    private int annualIncome;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoanStatus {
        private RepaymentType repaymentType;
        private LoanType loanType;
        private double principal;
        private double maturityPaymentAmount;
        private int term;
        private int gracePeriod;
        private double interestRate;

        public RepaymentCalcServiceRequest toRepaymentCalcServiceRequest() {
            return RepaymentCalcServiceRequest.builder()
                .repaymentType(repaymentType)
                .principal(principal)
                .term(term)
                .gracePeriod(gracePeriod)
                .interestRate(interestRate)
                .maturityPaymentAmount(maturityPaymentAmount)
                .build();
        }
    }
}
