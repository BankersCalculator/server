package com.bankersCalculator.server.calculator.dtiCalc.dto;

import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.common.enums.RepaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * 프로그램 설명 : 사용자가 입력한 데이터를 저장하는 DTO(Data Transfer Object) 클래스
 * 개발자 : 제갈명필
 * 수정  : 2024-07-13, 리펙토링.  참조 프로그램 -> DsrCalcRequest.java
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DtiCalcRequest {
    private List<LoanStatus> loanStatusList = new ArrayList<>();
    private Integer annualIncome;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString

    public static class LoanStatus {
        private RepaymentType repaymentType;
        private LoanType loanType;
        private Double principal;
        private Integer term;
        private Double interestRatePercentage;
    }

    public DtiCalcServiceRequest toServiceRequest() {
        List<DtiCalcServiceRequest.LoanStatus> serviceLoanStatusList = loanStatusList.stream()
            .map(loanStatus -> DtiCalcServiceRequest.LoanStatus.builder()
                .repaymentType(loanStatus.getRepaymentType())
                .loanType(loanStatus.getLoanType())
                .principal(loanStatus.getPrincipal())
                .term(loanStatus.getTerm())
                .interestRate(loanStatus.getInterestRatePercentage() / 100)
                .build())
            .collect(Collectors.toList());

        return DtiCalcServiceRequest.builder()
            .loanStatusList(serviceLoanStatusList)
            .annualIncome(annualIncome)
            .build();
    }
}
