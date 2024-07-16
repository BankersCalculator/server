package com.bankersCalculator.bankersCalculator.dtiCalc.calculator.DtiCalculatorImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.bankersCalculator.bankersCalculator.dtiCalc.dto.DtiCalcServiceRequest;
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
	
	@InjectMocks
    private BasicDtiCalc basicDtiCalc;

	@BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

	@Test
    public void testCalculate() {
        DtiCalcServiceRequest.LoanStatus loanStatus = DtiCalcServiceRequest.LoanStatus.builder()
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
