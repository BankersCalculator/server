package com.bankersCalculator.bankersCalculator.dtiCalc.service;


import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcRequest;
import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcResponse;
import org.springframework.stereotype.Service;

@Service
public class DtiCalcService {

    public DtiCalcResponse calculateDti(DtiCalcRequest request) {
    	 DtiCalcResponse response = new DtiCalcResponse();
         double annualPrincipalAndInterest;
        
         // 1) 상환방식에 따른, 원리금 상환금액 계산 
         if ("equalPayment".equals(request.getRepaymentMethod())) {
             annualPrincipalAndInterest = calculateEqualPayment(request.getApplicationAmount(), request.getInterestRate(), request.getLoanPeriod()) * 12;
         } else if ("equalPrincipal".equals(request.getRepaymentMethod())) {
             annualPrincipalAndInterest = calculateEqualPrincipal(request.getApplicationAmount(), request.getInterestRate(), request.getLoanPeriod());
         } else {
             throw new IllegalArgumentException("Invalid repayment method: " + request.getRepaymentMethod());
         }
         
         // 2) 기타대출 합산, 이자금액/ 기타대출의 이자율 (4% 가정)
         double totalOtherDebtInterest;
         totalOtherDebtInterest = (request.getSavingsLoanAmount() + request.getMortgageLoanAmount() + request.getOtherDebtAmount()) * 0.04;
         
         
         double dtiRatio = (annualPrincipalAndInterest + totalOtherDebtInterest / request.getAnnualIncome()) * 100;
         

         response.setAnnnualPrincipalAndInterest(annualPrincipalAndInterest);
         response.setDtiRatio(dtiRatio);

         return response;
    }
    
    private double calculateEqualPayment(double principal, double annualInterestRate, int years) {
        double monthlyInterestRate = annualInterestRate / 12 / 100;
        int numberOfMonths = years * 12;
        return principal * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfMonths)) / (Math.pow(1 + monthlyInterestRate, numberOfMonths) - 1);
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
