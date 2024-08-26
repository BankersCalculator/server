package com.bankersCalculator.server.calculator.dsrCalc.dto;

import com.bankersCalculator.server.common.enums.calculator.LoanType;
import com.bankersCalculator.server.common.enums.calculator.RepaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ToString

public class DsrCalcRequest {

    // TODO: @Valid 추가할 것.

    private List<LoanStatus> loanStatuses = new ArrayList<>();

    private Integer annualIncome;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class LoanStatus {
        private RepaymentType repaymentType;
        private LoanType loanType;
        private Double principal;
        private Double maturityPaymentAmount;
        private Integer term;
        private Integer gracePeriod;
        private Double interestRatePercentage;
    }

    public DsrCalcServiceRequest toServiceRequest() {
        List<DsrCalcServiceRequest.LoanStatus> serviceLoanStatusList = loanStatuses.stream()
            .map(loanStatus -> DsrCalcServiceRequest.LoanStatus.builder()
                .repaymentType(loanStatus.getRepaymentType())
                .loanType(loanStatus.getLoanType())
                .principal(loanStatus.getPrincipal())
                .maturityPaymentAmount(loanStatus.maturityPaymentAmount)
                .term(loanStatus.getTerm())
                .gracePeriod(loanStatus.getGracePeriod())
                .interestRate(loanStatus.getInterestRatePercentage() / 100)
                .build())
            .collect(Collectors.toList());

        return DsrCalcServiceRequest.builder()
            .loanStatusList(serviceLoanStatusList)
            .annualIncome(annualIncome)
            .build();
    }


}
