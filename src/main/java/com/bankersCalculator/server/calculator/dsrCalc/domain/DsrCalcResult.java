package com.bankersCalculator.server.calculator.dsrCalc.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DsrCalcResult {

    private int serial;
    private double principal;
    private double balance;
    private int term;
    private double annualPrincipalRepayment;
    private double annualInterestRepayment;

    @Builder
    public DsrCalcResult(int serial, double principal, double balance, int term, double annualPrincipalRepayment, double annualInterestRepayment) {
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
