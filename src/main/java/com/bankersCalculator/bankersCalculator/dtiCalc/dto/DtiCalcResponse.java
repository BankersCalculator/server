package com.bankersCalculator.bankersCalculator.dtiCalc.dto;
import java.math.BigDecimal;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public class DtiCalcResponse {
	private double annualPrincipalAndInterest;
    private double dtiRatio;
    private String ctt;

    // 지수제거하여, 숫자 표현 필요한 경우 사용 
    public BigDecimal getAnnualPrincipalAndInterest() {
    	return BigDecimal.valueOf(annualPrincipalAndInterest);
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

	public void setCtt(String ctt) {
		// TODO Auto-generated method stub
		this.ctt = ctt;
	}
	
	public String getCtt() {
		return ctt;
	}
}