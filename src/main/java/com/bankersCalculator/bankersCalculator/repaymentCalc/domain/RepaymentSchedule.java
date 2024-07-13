package com.bankersCalculator.bankersCalculator.repaymentCalc.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RepaymentSchedule {

    private int installmentNumber;
    private double principalPayment;
    private double interestPayment;
    private double totalPayment;
    private double remainingPrinciple;

    @Builder
    public RepaymentSchedule(int installmentNumber, double principalPayment, double interestPayment, double totalPayment, double remainingPrinciple) {
        this.installmentNumber = installmentNumber;
        this.principalPayment = principalPayment;
        this.interestPayment = interestPayment;
        this.totalPayment = totalPayment;
        this.remainingPrinciple = remainingPrinciple;
    }
}
