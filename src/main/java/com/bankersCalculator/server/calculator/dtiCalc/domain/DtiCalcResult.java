package com.bankersCalculator.server.calculator.dtiCalc.domain;

import lombok.Builder;
import lombok.Getter;


@Getter
public class DtiCalcResult {

    private int serial;
    private final double principal;
    private final double balance;
    private final int term;
    private final double annualPrincipalRepayment;
    private final double annualInterestRepayment;

    @Builder
    public DtiCalcResult(int serial, double principal, double balance, int term, double annualInterestRepayment, double annualPrincipalRepayment) {
        this.serial = serial;
        this.principal = principal;
        this.balance = balance;
        this.term = term;
        this.annualPrincipalRepayment = annualPrincipalRepayment;
        this.annualInterestRepayment = annualInterestRepayment;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }
}
