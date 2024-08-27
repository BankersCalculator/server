package com.bankersCalculator.server.calculator.dsrCalc.dto;

import com.bankersCalculator.server.calculator.repaymentCalc.dto.RepaymentCalcServiceRequest;
import com.bankersCalculator.server.common.enums.calculator.LoanType;
import com.bankersCalculator.server.common.enums.calculator.RepaymentType;
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

    private Integer annualIncome;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoanStatus {
        private RepaymentType repaymentType;
        private LoanType loanType;
        private Double principal;
        private Double maturityPaymentAmount;
        private Integer term;
        private Integer gracePeriod;
        private Double interestRate;

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
