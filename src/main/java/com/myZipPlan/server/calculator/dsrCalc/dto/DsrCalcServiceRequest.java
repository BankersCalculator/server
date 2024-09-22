package com.myZipPlan.server.calculator.dsrCalc.dto;

import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcServiceRequest;
import com.myZipPlan.server.common.enums.calculator.InterestRateType;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DsrCalcServiceRequest {


    private List<LoanStatus> loanStatusList;

    private BigDecimal annualIncome;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoanStatus {
        private RepaymentType repaymentType;
        private LoanType loanType;
        private BigDecimal principal;
        private BigDecimal maturityPaymentAmount;
        private BigDecimal term;
        private BigDecimal gracePeriod;
        private BigDecimal interestRate;
        private Boolean isMetroArea;
        private InterestRateType interestRateType;

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
