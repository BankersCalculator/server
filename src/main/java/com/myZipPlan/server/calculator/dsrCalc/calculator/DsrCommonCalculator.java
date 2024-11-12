package com.myZipPlan.server.calculator.dsrCalc.calculator;

import com.myZipPlan.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.myZipPlan.server.calculator.repaymentCalc.service.RepaymentCalcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
@Slf4j
public class DsrCommonCalculator {

    private final RepaymentCalcService repaymentCalcService;

    public DsrCalcResult dsrCalcForBulletLoan(DsrCalcServiceRequest.LoanStatus loanStatus, BigDecimal maxTerm) {
        BigDecimal principal = loanStatus.getPrincipal();
        BigDecimal term = loanStatus.getTerm();
        BigDecimal interestRate = loanStatus.getInterestRate();

        BigDecimal annualPrincipalRepayment = principal.divide(maxTerm, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(12)).setScale(0, RoundingMode.DOWN);
        BigDecimal annalInterestRepayment = principal.multiply(interestRate).setScale(0, RoundingMode.DOWN);

        return DsrCalcResult.builder()
            .loanDescription(loanStatus.getLoanType().getDescription())
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }

    public DsrCalcResult dsrCalcForAmortizingLoan(DsrCalcServiceRequest.LoanStatus loanStatus, BigDecimal maxTerm) {
        BigDecimal principal = loanStatus.getPrincipal();
        BigDecimal term = loanStatus.getTerm();

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculate(loanStatus.toRepaymentCalcServiceRequest());
        BigDecimal totalInterest = repaymentCalcResponse.getTotalInterest();

        BigDecimal annualPrincipalRepayment = principal.divide(maxTerm, 4, RoundingMode.DOWN).multiply(BigDecimal.valueOf(12)).setScale(0, RoundingMode.DOWN);
        BigDecimal annalInterestRepayment = totalInterest.divide(term, 4, RoundingMode.DOWN).multiply(BigDecimal.valueOf(12)).setScale(0, RoundingMode.DOWN);

        return DsrCalcResult.builder()
            .loanDescription(loanStatus.getLoanType().getDescription())
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }

    public DsrCalcResult dsrCalcForEqualPrincipalLoan(DsrCalcServiceRequest.LoanStatus loanStatus, BigDecimal maxTerm) {
        BigDecimal principal = loanStatus.getPrincipal();
        BigDecimal term = loanStatus.getTerm();

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculate(loanStatus.toRepaymentCalcServiceRequest());
        BigDecimal totalInterest = repaymentCalcResponse.getTotalInterest();

        BigDecimal annualPrincipalRepayment = principal.divide(maxTerm, 4, RoundingMode.DOWN).multiply(BigDecimal.valueOf(12)).setScale(0, RoundingMode.DOWN);
        BigDecimal annalInterestRepayment = totalInterest.divide(term, 4, RoundingMode.DOWN).multiply(BigDecimal.valueOf(12)).setScale(0, RoundingMode.DOWN);

        return DsrCalcResult.builder()
            .loanDescription(loanStatus.getLoanType().getDescription())
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }


    public DsrCalcResult dsrCalcWithoutPrincipalRepayment(DsrCalcServiceRequest.LoanStatus loanStatus) {
        BigDecimal principal = loanStatus.getPrincipal();
        BigDecimal term = loanStatus.getTerm();
        BigDecimal interestRate = loanStatus.getInterestRate();

        BigDecimal annualPrincipalRepayment = BigDecimal.ZERO;
        BigDecimal annalInterestRepayment = principal.multiply(interestRate);

        return DsrCalcResult.builder()
            .principal(principal)
            .term(term)
            .annualPrincipalRepayment(annualPrincipalRepayment)
            .annualInterestRepayment(annalInterestRepayment)
            .build();
    }
}
