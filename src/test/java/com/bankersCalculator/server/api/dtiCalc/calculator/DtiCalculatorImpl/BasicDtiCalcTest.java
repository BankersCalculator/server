package com.bankersCalculator.server.api.dtiCalc.calculator.DtiCalculatorImpl;

import com.bankersCalculator.server.calculator.dtiCalc.calculator.DtiCalculatorImpl.BasicDtiCalc;
import com.bankersCalculator.server.calculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.common.enums.RepaymentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BasicDtiCalcTest {

    @Autowired
    private BasicDtiCalc basicDtiCalc;




    @BeforeEach
    public void setUp() {
        // Optional setup code if necessary
    }
    
    DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Test
    public void testCalculateDti_withMortgageAndBullet() {
        DtiCalcServiceRequest.LoanStatus loanStatus = DtiCalcServiceRequest.LoanStatus.builder()
                .loanType(LoanType.MORTGAGE)
                .principal(400000000)
                .term(120)
                .interestRate(0.03)
                .repaymentType(RepaymentType.BULLET)
                .maturityPaymentAmount(0)
                .gracePeriod(0)
                .build();

        DtiCalcResult result = basicDtiCalc.calculateDti(loanStatus);
        
        

        assertNotNull(result);
        System.out.println("Principal: " + decimalFormat.format(result.getPrincipal()));
        System.out.println("Annual Interest Repayment: " + decimalFormat.format(result.getAnnualInterestRepayment()));
        System.out.println("Annual Principal Repayment: " + decimalFormat.format(result.getAnnualPrincipalRepayment()));
        //assertTrue(result.getAnnualInterestRepayment() > 0);
        
        //네이버 DTI, 만기상환 MAX = 120개월인 경우 
        assertEquals(12000000, result.getAnnualInterestRepayment(), 10);
        assertEquals(40000000, result.getAnnualPrincipalRepayment(), 10);
    }
    
    @Test
    public void testCalculateDti_withMortgageAndAmortizing() {
        DtiCalcServiceRequest.LoanStatus loanStatus = DtiCalcServiceRequest.LoanStatus.builder()
                .loanType(LoanType.MORTGAGE)
                .principal(400000000)
                .term(120)
                .interestRate(0.03)
                .repaymentType(RepaymentType.AMORTIZING)
                .maturityPaymentAmount(0)
                .gracePeriod(0)
                .build();

        

        DtiCalcResult result = basicDtiCalc.calculateDti(loanStatus);

        assertNotNull(result);
        
        System.out.println("Annual Interest Repayment: " + decimalFormat.format(result.getAnnualInterestRepayment()));
        System.out.println("Annual Principal Repayment: " + decimalFormat.format(result.getAnnualPrincipalRepayment()));
        
        assertEquals(6349158, result.getAnnualInterestRepayment(), 10);
        assertEquals(40000000, result.getAnnualPrincipalRepayment(), 10);
    }
    
    @Test
    public void testCalculateDti_withOtherLoan() {
        DtiCalcServiceRequest.LoanStatus loanStatus = DtiCalcServiceRequest.LoanStatus.builder()
                .loanType(LoanType.OTHER_LOAN)
                .principal(40000000)
                .term(120)
                .interestRate(0.03)
                .repaymentType(RepaymentType.EQUAL_PRINCIPAL)
                .maturityPaymentAmount(0)
                .gracePeriod(0)
                .build();

        DtiCalcResult result = basicDtiCalc.calculateDti(loanStatus);

        assertNotNull(result);
        
        System.out.println("Annual Interest Repayment: " + decimalFormat.format(result.getAnnualInterestRepayment()));
        System.out.println("Annual Principal Repayment: " + decimalFormat.format(result.getAnnualPrincipalRepayment()));

        assertEquals(1200000, result.getAnnualInterestRepayment(), 10);
    }
}
