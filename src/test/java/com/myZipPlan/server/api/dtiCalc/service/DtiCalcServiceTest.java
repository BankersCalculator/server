package com.myZipPlan.server.api.dtiCalc.service;

import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcResponse;
import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.myZipPlan.server.calculator.dtiCalc.service.DtiCalcService;
import com.myZipPlan.server.common.enums.calculator.LoanType;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
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
                .principal(400000000.0)
                .term(120)
                .interestRate(0.03)
                .build();

        DtiCalcServiceRequest.LoanStatus loan2 = DtiCalcServiceRequest.LoanStatus.builder()
                .loanType(LoanType.OTHER_LOAN)
                .repaymentType(RepaymentType.EQUAL_PRINCIPAL)
                .principal(40000000.0)
                .term(360)
                .interestRate(0.04)
                .build();

        DtiCalcServiceRequest.LoanStatus loan3 = DtiCalcServiceRequest.LoanStatus.builder()
                .loanType(LoanType.PERSONAL_LOAN)
                .repaymentType(RepaymentType.BULLET)
                .principal(0.0)
                .term(12)
                .interestRate(0.03)
                .build();

        DtiCalcServiceRequest request = DtiCalcServiceRequest.builder()
                .annualIncome(40000000)
                .loanStatusList(Arrays.asList(loan1, loan2, loan3))
                .build();

        DtiCalcResponse response = dtiCalcService.dtiCalculate(request);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");


        // 결과를 출력
        System.out.println("Annual Interest Repayment for Loan 1: " + decimalFormat.format(response.getDtiCalcResults().get(0).getAnnualInterestRepayment()));
        System.out.println("Annual Interest Repayment for Loan 2: " + decimalFormat.format(response.getDtiCalcResults().get(1).getAnnualInterestRepayment()));
        System.out.println("Annual Interest Repayment for Loan 3: " + decimalFormat.format(response.getDtiCalcResults().get(2).getAnnualInterestRepayment()));

        System.out.println("Final DTI Ratio: " + Math.round(response.getFinalDtiRatio()));
        System.out.println("Total Loan Count: " + decimalFormat.format(response.getTotalLoanCount()));
        assertEquals(119.87, response.getFinalDtiRatio(), 1);
    }
}
