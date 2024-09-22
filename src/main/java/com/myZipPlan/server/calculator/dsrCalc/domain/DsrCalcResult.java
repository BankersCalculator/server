package com.myZipPlan.server.calculator.dsrCalc.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class DsrCalcResult {

    private BigDecimal serial;
    private BigDecimal principal;
    private BigDecimal balance;
    private BigDecimal term;
    private BigDecimal annualPrincipalRepayment;
    private BigDecimal annualInterestRepayment;

    @Builder
    public DsrCalcResult(BigDecimal serial, BigDecimal principal, BigDecimal balance, BigDecimal term, BigDecimal annualPrincipalRepayment, BigDecimal annualInterestRepayment) {
        this.serial = serial;
        this.principal = principal;
        this.balance = balance;
        this.term = term;
        this.annualPrincipalRepayment = annualPrincipalRepayment;
        this.annualInterestRepayment = annualInterestRepayment;
    }

    public void setSerial(BigDecimal serial) {
        this.serial = serial;
    }
}
