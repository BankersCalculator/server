package com.myZipPlan.server.calculator.dsrCalc.dto;

import com.myZipPlan.server.common.enums.calculator.InterestRateType;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Slf4j
public class DsrCalcRequest {

    // TODO: @Valid 추가할 것.

    private List<LoanStatus> loanStatuses = new ArrayList<>();

    private BigDecimal annualIncome;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class LoanStatus {
        private RepaymentType repaymentType;
        private LoanType loanType;
        private BigDecimal principal;
        private BigDecimal maturityPaymentAmount;
        private BigDecimal term;
        private BigDecimal gracePeriod;
        private BigDecimal interestRatePercentage;
        private Boolean isMetroArea;
        private InterestRateType interestRateType;
    }

    public DsrCalcServiceRequest toServiceRequest() {
        List<DsrCalcServiceRequest.LoanStatus> serviceLoanStatusList = loanStatuses.stream()
            .map(loanStatus -> {
                BigDecimal interestRate = loanStatus.getInterestRatePercentage().divide(BigDecimal.valueOf(100), 4, RoundingMode.DOWN);
                BigDecimal interestRateAddition = getInterestRateAddition(loanStatus.getIsMetroArea(), loanStatus.getInterestRateType());

                log.info("interestRate: {}", interestRate);
                log.info("interestRateAddition: {}", interestRateAddition);
                interestRate = interestRate.add(interestRateAddition);
                log.info("interestRate: {}", interestRate);
                return DsrCalcServiceRequest.LoanStatus.builder()
                    .repaymentType(loanStatus.getRepaymentType())
                    .loanType(loanStatus.getLoanType())
                    .principal(loanStatus.getPrincipal())
                    .maturityPaymentAmount(loanStatus.maturityPaymentAmount)
                    .term(loanStatus.getTerm())
                    .gracePeriod(loanStatus.getGracePeriod())
                    .interestRate(interestRate)
                    .isMetroArea(loanStatus.getIsMetroArea())
                    .interestRateType(loanStatus.getInterestRateType())
                    .build();

            })
            .collect(Collectors.toList());
        return DsrCalcServiceRequest.builder()
            .loanStatusList(serviceLoanStatusList)
            .annualIncome(annualIncome)
            .build();
    }

    // interestRateType에 따른 가산금리 계산
    private BigDecimal getInterestRateAddition(Boolean isMetroArea, InterestRateType interestRateType) {
        if (isMetroArea) {
            return interestRateType.getMetroAreaInterestRateAddition();
        } else {
            return interestRateType.getNonMetroAreaInterestRateAddition();
        }

    }


}
