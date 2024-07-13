package com.bankersCalculator.bankersCalculator.dsrCalc.dto;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import com.bankersCalculator.bankersCalculator.repaymentCalc.dto.RepaymentCalcServiceRequest;
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
