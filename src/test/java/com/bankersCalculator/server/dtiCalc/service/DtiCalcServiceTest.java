package com.bankersCalculator.server.dtiCalc.service;

import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcResponse;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.bankersCalculator.server.calculator.dtiCalc.service.DtiCalcService;
import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.common.enums.RepaymentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DecimalFormat;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DtiCalcServiceTest {

	@Autowired
    private DtiCalcService dtiCalcService;

    @Test
    public void testDtiCalculate_withMultipleLoans() {
        DtiCalcServiceRequest.LoanStatus loan1 = DtiCalcServiceRequest.LoanStatus.builder()
        		.loanType(LoanType.MORTGAGE)
        		.repaymentType(RepaymentType.AMORTIZING)
        		.principal(400000000)
                .term(120)
                .interestRate(0.03)
                .maturityPaymentAmount(0)
                .gracePeriod(0)
                .build();
        
        DtiCalcServiceRequest.LoanStatus loan2 = DtiCalcServiceRequest.LoanStatus.builder()
        		.loanType(LoanType.OTHER_LOAN)
        		.repaymentType(RepaymentType.EQUAL_PRINCIPAL)
        		.principal(40000000)
                .term(360)
                .interestRate(0.04)
                .maturityPaymentAmount(0)
                .gracePeriod(0)
                .build();
        
        DtiCalcServiceRequest.LoanStatus loan3 = DtiCalcServiceRequest.LoanStatus.builder()
        		.loanType(LoanType.PERSONAL_LOAN)
        		.repaymentType(RepaymentType.BULLET)
        		.principal(0)
                .term(12)
                .interestRate(0.03)
                .maturityPaymentAmount(0)
                .gracePeriod(0)
                .build();
        
        DtiCalcServiceRequest request = DtiCalcServiceRequest.builder()
                .annualIncome(40000000)
                .loanStatusList(Arrays.asList(loan1, loan2, loan3))
                .build();
        
        DtiCalcResponse response = dtiCalcService.dticalculate(request);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");



        // 결과를 출력
        System.out.println("Annual Principal Repayment for Loan 1: " + decimalFormat.format(response.getDtiCalcResultList().get(0).getAnnualPrincipalRepayment()));
        System.out.println("Annual Interest Repayment for Loan 1: " + decimalFormat.format(response.getDtiCalcResultList().get(0).getAnnualInterestRepayment()));
        
        System.out.println("Annual Principal Repayment for Loan 2: " + decimalFormat.format(response.getDtiCalcResultList().get(1).getAnnualPrincipalRepayment()));
        System.out.println("Annual Interest Repayment for Loan 2: " + decimalFormat.format(response.getDtiCalcResultList().get(1).getAnnualInterestRepayment()));
        
        System.out.println("Annual Principal Repayment for Loan 3: " + decimalFormat.format(response.getDtiCalcResultList().get(2).getAnnualPrincipalRepayment()));
        System.out.println("Annual Interest Repayment for Loan 3: " + decimalFormat.format(response.getDtiCalcResultList().get(2).getAnnualInterestRepayment()));
        
        System.out.println("Final DTI Ratio: " + Math.round(response.getFinalDtiRatio()));
        System.out.println("Total Loan Count: " + decimalFormat.format(response.getTotalLoanCount()));
        assertEquals(119.87, response.getFinalDtiRatio(), 1);
    }
    
    /*
    @Test
    public void testDtiCalculate_withNoLoans() {
        DtiCalcServiceRequest request = DtiCalcServiceRequest.builder()
                .annualIncome(100000000)
                .loanStatusList(Collections.emptyList())
                .build();

        DtiCalcResponse response = dtiCalcService.dticalculate(request);

        assertEquals(100000000, response.getAnnualIncome());
        assertEquals(0, response.getTotalLoanCount());
        assertEquals(0, response.getFinalDtiRatio(), 0.01);
    }
    
    

    @Test
    public void testDtiCalculate_withSingleLoan() {
        DtiCalcServiceRequest.LoanStatus loan = DtiCalcServiceRequest.LoanStatus.builder()
                .principal(50000000)
                .term(240)
                .interestRate(0.03)
                .repaymentType(RepaymentType.AMORTIZING)
                .gracePeriod(12)
                .build();

        DtiCalcResult result = DtiCalcResult.builder()
                .annualPrincipalRepayment(2500000)
                .annualInterestRepayment(1500000)
                .build();

        when(dtiCalculator.calculateDti(loan)).thenReturn(result);

        DtiCalcServiceRequest request = DtiCalcServiceRequest.builder()
                .annualIncome(50000000)
                .loanStatusList(Collections.singletonList(loan))
                .build();

        DtiCalcResponse response = dtiCalcService.dticalculate(request);

        assertEquals(50000000, response.getAnnualIncome());
        assertEquals(1, response.getTotalLoanCount());
        assertEquals(2500000, response.getDtiCalcResultList().get(0).getAnnualPrincipalRepayment());
        assertEquals(1500000, response.getDtiCalcResultList().get(0).getAnnualInterestRepayment());
        assertEquals(((2500000 + 1500000) / 50000000) * 100, response.getFinalDtiRatio(), 0.01);
    }
    */
}
