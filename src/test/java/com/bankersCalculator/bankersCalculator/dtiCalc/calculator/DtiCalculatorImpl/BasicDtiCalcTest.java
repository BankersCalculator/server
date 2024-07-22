package com.bankersCalculator.bankersCalculator.dtiCalc.calculator.DtiCalculatorImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.common.enums.RepaymentType;
import com.bankersCalculator.bankersCalculator.dtiCalc.calculator.DtiCommonCalculator;
import com.bankersCalculator.bankersCalculator.dtiCalc.domain.DtiCalcResult;
import com.bankersCalculator.bankersCalculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.bankersCalculator.bankersCalculator.repaymentCalc.service.RepaymentCalcService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class BasicDtiCalcTest {
	 @Mock
	    private RepaymentCalcService repaymentCalcService;

	    @Mock
	    private DtiCommonCalculator dtiCommonCalculator;

	    @InjectMocks
	    private BasicDtiCalc basicDtiCalc;

	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testCalculateDtiForMortgageLoanBullet() {
	        DtiCalcServiceRequest.LoanStatus loanStatus = DtiCalcServiceRequest.LoanStatus.builder()
	            .loanType(LoanType.MORTGAGE)
	            .repaymentType(RepaymentType.BULLET)
	            .principal(5000000)
	            .term(30)
	            .interestRate(3 / 100)
	            .build();

	        DtiCalcResult expectedDtiCalcResult = DtiCalcResult.builder()
	            .principal(5000000)
	            .term(30)
	            .annualPrincipalRepayment(200000)
	            .annualInterestRepayment(175000)
	            .build();
	        
	       

	        when(dtiCommonCalculator.dtiCalcForBulletLoan(loanStatus, 30)).thenReturn(expectedDtiCalcResult);
	        
	        System.out.println();

	        DtiCalcResult result = basicDtiCalc.calculateDti(loanStatus);

	        assertEquals(expectedDtiCalcResult, result);
	    }

	    @Test
	    public void testCalculateDtiForOtherLoans() {
	        DtiCalcServiceRequest.LoanStatus loanStatus = DtiCalcServiceRequest.LoanStatus.builder()
	            .loanType(LoanType.JEONSE_DEPOSIT_COLLATERAL_LOAN)
	            .principal(5000000)
	            .interestRate(3.5 / 100)
	            .build();

	        DtiCalcResult result = basicDtiCalc.calculateDti(loanStatus);

	        assertEquals(5000000, result.getPrincipal(), 0.01);
	        assertEquals(175000, result.getAnnualInterestRepayment(), 0.01);
	    }

	    @Test
	    public void testCalculateDtiForAmortizingLoan() {
	        DtiCalcServiceRequest.LoanStatus loanStatus = DtiCalcServiceRequest.LoanStatus.builder()
	            .loanType(LoanType.MORTGAGE)
	            .repaymentType(RepaymentType.AMORTIZING)
	            .principal(5000000)
	            .maturityPaymentAmount(1000000)
	            .term(30)
	            .gracePeriod(5)
	            .interestRate(3.5 / 100)
	            .build();

	        RepaymentCalcResponse repaymentCalcResponse = RepaymentCalcResponse.builder()
	            .totalInterest(100000)
	            .build();

	        when(repaymentCalcService.calculateRepayment(loanStatus.toRepaymentCalcServiceRequest()))
	            .thenReturn(repaymentCalcResponse);

	        DtiCalcResult result = basicDtiCalc.calculateDti(loanStatus);

	        assertEquals(5000000, result.getPrincipal(), 0.01);
	        assertEquals(30, result.getTerm());
	        assertEquals(200000.00, result.getAnnualPrincipalRepayment(), 0.01);
	        assertEquals(3333.33, result.getAnnualInterestRepayment(), 0.01);
	    }
	

}
