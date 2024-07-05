package com.bankersCalculator.bankersCalculator.dtiCalc.dto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public class DtiCalcResponse {
	private double annualPrincipalAndInterest;
    private double dtiRatio;

    // Getters and Setters
    public double getAnnualPrincipalAndInterest() {
        return annualPrincipalAndInterest;
    }

    public void setAnnnualPrincipalAndInterest(double annualPrincipalAndInterest) {
        this.annualPrincipalAndInterest = annualPrincipalAndInterest;
    }

    public double getDtiRatio() {
        return dtiRatio;
    }

    public void setDtiRatio(double dtiRatio) {
        this.dtiRatio = dtiRatio;
    }
}