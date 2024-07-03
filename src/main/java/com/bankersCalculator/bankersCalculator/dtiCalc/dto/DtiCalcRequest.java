package com.bankersCalculator.bankersCalculator.dtiCalc.dto;

import lombok.Data;


public class DtiCalcRequest {
	
	private double applicationAmount;   
	private double maturityRepaymentAmount;
	private String repaymentMethod;
	private int loanPriod;
	private int gracePeriod; //거치기간
	private double interestRate;
	private double repaymentAmount;
	private double savingsLoanAmount;
	private double mortgageLoanAmount;
	private double otehrDebtAmount;
	private double annalIncome;
	private String baseDate;
	private double currentAnnualRepaymnetOrinterest; //현재납부중인 연원리금 혹은 연이자 

}
