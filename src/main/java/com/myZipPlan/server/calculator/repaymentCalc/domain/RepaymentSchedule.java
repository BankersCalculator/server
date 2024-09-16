package com.myZipPlan.server.calculator.repaymentCalc.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RepaymentSchedule {

    private int installmentNumber; // 상환회차
    private double principalPayment; // 상환원금
    private double interestPayment; // 상환이자
    private double totalPayment; // 총 상환액
    private double remainingPrincipal; // 잔여원금

    @Builder
    public RepaymentSchedule(int installmentNumber, double principalPayment, double interestPayment, double totalPayment, double remainingPrincipal) {
        this.installmentNumber = installmentNumber;
        this.principalPayment = principalPayment;
        this.interestPayment = interestPayment;
        this.totalPayment = totalPayment;
        this.remainingPrincipal = remainingPrincipal;
    }
}
