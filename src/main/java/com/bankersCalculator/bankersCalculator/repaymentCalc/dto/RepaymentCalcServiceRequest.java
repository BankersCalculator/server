package com.bankersCalculator.bankersCalculator.repaymentCalc.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RepaymentCalcServiceRequest {

    private RepaymentType type;

    private double principal; // 원금

    private int term; // 기간(개월수)

    private int gracePeriod; // 거치기간

    private double interestRate; // 연이자율

    @Builder
    public RepaymentCalcServiceRequest(double principal, int term, int gracePeriod, double interestRate, RepaymentType type) {
        this.principal = principal;
        this.term = term;
        this.gracePeriod = gracePeriod;
        this.interestRate = interestRate;
        this.type = type;
    }

    public double getInterestRateAsDecimal() {
        return interestRate / 100.0 ;
    }
}
