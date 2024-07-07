package com.bankersCalculator.bankersCalculator.dsrCalc.dto;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class DsrCalcRequest {

    // TODO: @Valid 추가할 것.

    private List<LoanStatus> loanStatusList = new ArrayList<>();

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

    public DsrCalcServiceRequest toServiceRequest() {
        List<DsrCalcServiceRequest.LoanStatus> serviceLoanStatusList = loanStatusList.stream()
            .map(loanStatus -> DsrCalcServiceRequest.LoanStatus.builder()
                .repaymentType(loanStatus.getRepaymentType())
                .loanType(loanStatus.getLoanType())
                .principal(loanStatus.getPrincipal())
                .term(loanStatus.getTerm())
                .gracePeriod(loanStatus.getGracePeriod())
                .remainingTerm(loanStatus.getRemainingTerm())
                .interestRate(loanStatus.getInterestRate())
                .build())
            .collect(Collectors.toList());

        return DsrCalcServiceRequest.builder()
            .loanStatusList(serviceLoanStatusList)
            .annualIncome(annualIncome)
            .build();
    }



}
