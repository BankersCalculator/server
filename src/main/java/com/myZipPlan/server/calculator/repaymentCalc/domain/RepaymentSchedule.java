package com.myZipPlan.server.calculator.repaymentCalc.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RepaymentSchedule {

    private BigDecimal installmentNumber; // 상환회차
    private BigDecimal principalPayment; // 상환원금
    private BigDecimal interestPayment; // 상환이자
    private BigDecimal totalPayment; // 총 상환액
    private BigDecimal remainingPrincipal; // 잔여원금

    @Builder
    public RepaymentSchedule(BigDecimal installmentNumber, BigDecimal principalPayment, BigDecimal interestPayment, BigDecimal totalPayment, BigDecimal remainingPrincipal) {
        this.installmentNumber = installmentNumber;
        this.principalPayment = principalPayment;
        this.interestPayment = interestPayment;
        this.totalPayment = totalPayment;
        this.remainingPrincipal = remainingPrincipal;
    }
}
