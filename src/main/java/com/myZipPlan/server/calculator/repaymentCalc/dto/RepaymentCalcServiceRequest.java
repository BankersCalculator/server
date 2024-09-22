package com.myZipPlan.server.calculator.repaymentCalc.dto;

import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RepaymentCalcServiceRequest {

    private RepaymentType repaymentType;

    private BigDecimal principal; // 원금

    private BigDecimal term; // 기간(개월수)

    private BigDecimal gracePeriod; // 거치기간

    private BigDecimal interestRate; // 연이자율

    private BigDecimal maturityPaymentAmount; // 만기상환액


    @Builder
    public RepaymentCalcServiceRequest(BigDecimal principal, BigDecimal term, BigDecimal gracePeriod, BigDecimal interestRate, RepaymentType repaymentType, BigDecimal maturityPaymentAmount) {
        this.principal = principal;
        this.term = term;
        this.gracePeriod = gracePeriod;
        this.interestRate = interestRate;
        this.repaymentType = repaymentType;
        this.maturityPaymentAmount = maturityPaymentAmount;
    }
}
