package com.bankersCalculator.bankersCalculator.dtiCalc.service;


import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcRequest;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcResponse;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class DtiCalcService {

	
    public DtiCalcResponse calculateDti(DtiCalcRequest request) {
    	 DtiCalcResponse response = new DtiCalcResponse();
         double annualPrincipalAndInterest;
         String ctt = "";
         // 1) 상환방식에 따른, 원리금 상환금액 계산 
         if ("equalPayment".equals(request.getRepaymentMethod())) {
             annualPrincipalAndInterest = calculateEqualPayment(request.getApplicationAmount(), request.getInterestRate(), request.getLoanPeriod());
             
         } else if ("equalPrincipal".equals(request.getRepaymentMethod())) {
             annualPrincipalAndInterest = calculateEqualPrincipal(request.getApplicationAmount(), request.getInterestRate(), request.getLoanPeriod());
         } else {
             throw new IllegalArgumentException("Invalid repayment method: " + request.getRepaymentMethod());
         }
         
         // 2) 기타대출 합산, 이자금액/ 기타대출의 이자율 (4% 가정)
         double totalOtherDebtInterest;
         totalOtherDebtInterest = (request.getSavingsLoanAmount() + request.getMortgageLoanAmount() + request.getOtherDebtAmount()) * 0.04;
         
         
         double dtiRatio = Math.round(((annualPrincipalAndInterest + totalOtherDebtInterest) / request.getAnnualIncome()) * 100 * 100)/100.0;
         
         System.out.println(dtiRatio);  // 출력: "123.456"
         System.out.println(Math.round(dtiRatio*100)/100.0);  // 출력: "123.456"


         ctt = "주택담보대출 " + BigDecimal.valueOf(Math.round(request.getApplicationAmount()))  + "원의 경우, <br>"  
                 + "연 원리금상환금액은 " + BigDecimal.valueOf(annualPrincipalAndInterest) + "원 이고 <br>" 
                 + "(" + request.getLoanPeriod() + "년, " + request.getInterestRate() + "%, " + "원리금균등 기준)<br>"
                 
                 + "연소득은 " + BigDecimal.valueOf(Math.round(request.getAnnualIncome())) + "원 이므로<br>"
                 + "DTI는 " + dtiRatio + "%로 예상됩니다.";
         
         
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
}
