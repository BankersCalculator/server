package com.bankersCalculator.bankersCalculator.dtiCalc.dto;

import java.math.BigDecimal;

public class DtiCalcRequest {
    private double applicationAmount;
    private double maturityRepaymentAmount;
    private String repaymentMethod;
    private int loanPeriod;
    private int gracePeriod;
    private double interestRate;
    private double repaymentAmount;
    private double savingsLoanAmount;
    private double mortgageLoanAmount;
    private double otherDebtAmount;
    private double annualIncome;
    private String baseDate;
    private double currentAnnualRepaymentOrInterest;


    
    public double getApplicationAmount() {
        //return applicationAmount != null ? applicationAmount.doubleValue() : 0.0;
    	return applicationAmount;
    }
    
    public BigDecimal getApplicationAmount2() {
        //return applicationAmount != null ? applicationAmount.doubleValue() : 0.0;
    	System.out.println(BigDecimal.valueOf(applicationAmount));
    	return BigDecimal.valueOf(applicationAmount);
    }
    

    public void setApplicationAmount(double applicationAmount) {
        this.applicationAmount = applicationAmount;
    }

    public double getMaturityRepaymentAmount() {
        return maturityRepaymentAmount;
    }

    public void setMaturityRepaymentAmount(double maturityRepaymentAmount) {
        this.maturityRepaymentAmount = maturityRepaymentAmount;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public int getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(int loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(int gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(double repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public double getSavingsLoanAmount() {
        return savingsLoanAmount;
    }

    public void setSavingsLoanAmount(double savingsLoanAmount) {
        this.savingsLoanAmount = savingsLoanAmount;
    }

    public double getMortgageLoanAmount() {
        return mortgageLoanAmount;
    }

    public void setMortgageLoanAmount(double mortgageLoanAmount) {
        this.mortgageLoanAmount = mortgageLoanAmount;
    }

    public double getOtherDebtAmount() {
        return otherDebtAmount;
    }

    public void setOtherDebtAmount(double otherDebtAmount) {
        this.otherDebtAmount = otherDebtAmount;
    }

    public double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    public double getCurrentAnnualRepaymentOrInterest() {
        return currentAnnualRepaymentOrInterest;
    }

    public void setCurrentAnnualRepaymentOrInterest(double currentAnnualRepaymentOrInterest) {
        this.currentAnnualRepaymentOrInterest = currentAnnualRepaymentOrInterest;
    }

	public double getOtherDebtsTotalAmount() {
		return otherDebtAmount;
	    
	}
}
