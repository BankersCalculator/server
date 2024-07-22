package com.bankersCalculator.server.repaymentCalc.dto;

import com.bankersCalculator.server.common.enums.RepaymentType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RepaymentCalcServiceRequest {

    private RepaymentType repaymentType;

    private double principal; // 원금

    private int term; // 기간(개월수)

    private int gracePeriod; // 거치기간

    private double interestRate; // 연이자율

    private double maturityPaymentAmount; // 만기상환액


    @Builder
    public RepaymentCalcServiceRequest(double principal, int term, int gracePeriod, double interestRate, RepaymentType repaymentType, double maturityPaymentAmount) {
        this.principal = principal;
        this.term = term;
        this.gracePeriod = gracePeriod;
        this.interestRate = interestRate;
        this.repaymentType = repaymentType;
        this.maturityPaymentAmount = maturityPaymentAmount;
    }
}
