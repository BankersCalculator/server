package com.bankersCalculator.bankersCalculator.repaymentCalc.dto;

import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepaymentCalcRequest {

    // TODO: @Valid 적용할 것.

    @NotNull
    private RepaymentType repaymentType;
//    @Min(1000000)
    private double principal; // 원금
//    @Range(min = 1, max = 600)
    private int term; // 기간(개월수)
//    @Range(min = 0, max = 600)
    private int gracePeriod; // 거치기간
//    @Range(min = 0, max = 20)
    private double interestRatePercentage; // 연이자율


    public RepaymentCalcServiceRequest toServiceRequest() {
        return RepaymentCalcServiceRequest.builder()
            .repaymentType(repaymentType)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRatePercentage / 100)
            .build();
    }
}
