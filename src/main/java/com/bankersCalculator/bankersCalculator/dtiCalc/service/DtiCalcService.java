package com.bankersCalculator.bankersCalculator.dtiCalc.service;


import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcServiceRequest;

import lombok.RequiredArgsConstructor;

import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcResponse;
import com.bankersCalculator.bankersCalculator.dtiCalc.calculator.DtiCalculator;
import com.bankersCalculator.bankersCalculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.bankersCalculator.bankersCalculator.dtiCalc.calculator.DtiCalculator;
import com.bankersCalculator.bankersCalculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcRequest;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcResponse;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DtiCalcService {
	

	/* 
	 * 프로그램 설명 :DTI 계산기 Service 
	 * 개발자 : 제갈명필
	 * 수정  : 2024-07-13, 리펙토링.  
	 */
	
	private DtiCalculator dtiCalculator;
    public DtiCalcResponse dticalculate(DtiCalcServiceRequest request) {
    	double totalDtiAmount = 0;
    	int annualIncome = request.getAnnualIncome();
    	int totalLoanCount = 0;
    	   
    	List<DtiCalcResult> dtiCalcResultList = new ArrayList<>();
    	for (DtiCalcServiceRequest.LoanStatus loanStatus : request.getLoanStatusList()) { 
    		DtiCalcResult dtiCalcResult = dtiCalculator.calculateDti(loanStatus);
    		
    		dtiCalcResult.setSerial(++totalLoanCount);
    		totalDtiAmount += dtiCalcResult.getAnnualPrincipalRepayment();            
            totalDtiAmount += dtiCalcResult.getAnnualInterestRepayment();

            dtiCalcResultList.add(dtiCalcResult);
    	}
    	
    	double totalDtiRatio = (totalDtiAmount / annualIncome);

        return DtiCalcResponse.builder()
            .annualIncome(annualIncome)
            .totalLoanCount(totalLoanCount)
            .dtiCalcResultList(dtiCalcResultList)
            .finalDtiRatio(totalDtiRatio)
            .build();
    }   	 
}
    /*
    	 DtiCalcResponse response = new DtiCalcResponse();
    	 DecimalFormat decimalFormat = new DecimalFormat("#,###");
         double annualPrincipalAndInterest;
         String repaymentType = "" ;
	     String ctt = "";	     
	    
         // 1) 기타대출 합산, 이자금액/ 기타대출의 이자율 (4% 가정)
         double totalOtherDebtInterest = (request.getSavingsLoanAmount() + request.getOtherDebtAmount()) * 0.04;
         
         
         // 2) 상환방식에 따른, 원리금 상환금액 계산 
         if ("equalPayment".equals(request.getRepaymentMethod())) {
        	 repaymentType = "원리금균등";
             annualPrincipalAndInterest = calculateEqualPayment(request.getApplicationAmount() + request.getMortgageLoanAmount() - request.getRepaymentAmount(), request.getInterestRate(), request.getLoanPeriod());
             
             
         } else if ("equalPrincipal".equals(request.getRepaymentMethod())) {
        	 repaymentType = "원금균등";
             annualPrincipalAndInterest = calculateEqualPrincipal(request.getApplicationAmount() + request.getMortgageLoanAmount() - request.getRepaymentAmount(), request.getInterestRate(), request.getLoanPeriod());
         } else {
             throw new IllegalArgumentException("Invalid repayment method: " + request.getRepaymentMethod());
         }
         
         
         double dtiRatio = Math.round(((annualPrincipalAndInterest + totalOtherDebtInterest) / request.getAnnualIncome()) * 100 * 100)/100.0;
         System.out.println(dtiRatio);  
         System.out.println(Math.round(dtiRatio*100)/100.0);  
         
         
         System.out.println(decimalFormat.format(request.getApplicationAmount()));
         
         //decimalFormat.format(BigDecimal.valueOf(Math.round(request.getApplicationAmount())))
         ctt = "주택담보대출 " + decimalFormat.format(request.getApplicationAmount())  + "원의 경우, <br>"  
                 + "연 원리금상환금액은 " + decimalFormat.format(annualPrincipalAndInterest) + "원 이고 <br>" 
                 + "(" + request.getLoanPeriod() + "년, " + request.getInterestRate() + "%, " + repaymentType + "기준)<br>"
                 + "연소득은 " + decimalFormat.format(request.getAnnualIncome())+ "원 이므로<br>"
                 + "DTI는 " + dtiRatio + "%로 예상됩니다."
               ;
        
         
         
         


         
         
         
         response.setCtt(ctt);
         response.setAnnnualPrincipalAndInterest(annualPrincipalAndInterest);
         response.setDtiRatio(dtiRatio);
         return response;
    }
    
    private double calculateEqualPayment(double principal, double annualInterestRate, int years) {
        double monthlyInterestRate = annualInterestRate / 12 / 100;
        int numberOfMonths = years * 12;
        return Math.round(principal * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfMonths)) / (Math.pow(1 + monthlyInterestRate, numberOfMonths) - 1)*12);
    }

    private double calculateEqualPrincipal(double principal, double annualInterestRate, int years) {
        double monthlyInterestRate = annualInterestRate / 12 / 100;
        int numberOfMonths = years * 12;
        double totalRepayment = 0.0;
        
        for (int month = 1; month <= numberOfMonths; month++) {
            double monthlyPrincipalRepayment = principal / numberOfMonths;
            double monthlyInterestRepayment = (principal - (month - 1) * monthlyPrincipalRepayment) * monthlyInterestRate;
            totalRepayment += monthlyPrincipalRepayment + monthlyInterestRepayment;
        }

        return totalRepayment;
    }
    */
