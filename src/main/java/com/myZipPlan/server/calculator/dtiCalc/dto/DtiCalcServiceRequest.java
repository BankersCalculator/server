package com.myZipPlan.server.calculator.dtiCalc.dto;

import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcServiceRequest;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtiCalcServiceRequest {
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
        private Integer term;
        private Double interestRate;


        public RepaymentCalcServiceRequest toRepaymentCalcServiceRequest() {
            return RepaymentCalcServiceRequest.builder()
                .repaymentType(repaymentType)
                .principal(principal)
                .term(term)
                .interestRate(interestRate)
                .build();
        }
    }
}

