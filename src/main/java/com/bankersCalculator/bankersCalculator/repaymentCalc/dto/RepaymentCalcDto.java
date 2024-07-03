package com.bankersCalculator.bankersCalculator.repaymentCalc.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RepaymentCalcDto {

    private double principal; // 원금

    private int term; // 기간(개월수)

    private int gracePeriod; // 거치기간

    private double interestRate; // 연이자율

    @Builder
    public RepaymentCalcDto(double principal, int term, int gracePeriod, double interestRate) {
        this.principal = principal;
        this.term = term;
        this.gracePeriod = gracePeriod;
        this.interestRate = interestRate;
    }
}
