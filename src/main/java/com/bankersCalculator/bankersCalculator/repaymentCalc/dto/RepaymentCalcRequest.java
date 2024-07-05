package com.bankersCalculator.bankersCalculator.repaymentCalc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
public class RepaymentCalcRequest {

    @NotNull
    private RepaymentType type;
//    @Min(1000000)
    private double principal; // 원금
//    @Range(min = 1, max = 600)
    private int term; // 기간(개월수)
//    @Range(min = 0, max = 600)
    private int gracePeriod; // 거치기간
//    @Range(min = 0, max = 20)
    private double interestRate; // 연이자율


    @Builder
    public RepaymentCalcRequest(double principal, int term, int gracePeriod, double interestRate, RepaymentType type) {
        this.principal = principal;
        this.term = term;
        this.gracePeriod = gracePeriod;
        this.interestRate = interestRate;
        this.type = type;
    }

    public RepaymentCalcServiceRequest toServiceRequest() {
        return RepaymentCalcServiceRequest.builder()
            .type(type)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRate)
            .build();
    }
}
