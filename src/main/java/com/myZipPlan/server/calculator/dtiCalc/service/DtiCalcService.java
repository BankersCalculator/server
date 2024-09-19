package com.myZipPlan.server.calculator.dtiCalc.service;

import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcResponse;
import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcServiceRequest;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcResponse;
import com.myZipPlan.server.calculator.repaymentCalc.dto.RepaymentCalcServiceRequest;
import com.myZipPlan.server.calculator.repaymentCalc.service.RepaymentCalcService;
import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
@Slf4j
public class DtiCalcService {

    private final RepaymentCalcService repaymentCalcService;

    public DtiCalcResponse dtiCalculate(DtiCalcServiceRequest request) {

        BigDecimal loanAmount = request.getLoanAmount();
        BigDecimal interestRate = request.getInterestRate();
        Integer loanTerm = request.getLoanTerm();
        RepaymentType repaymentType = request.getRepaymentType();
        BigDecimal yearlyLoanInterestRepayment = request.getYearlyLoanInterestRepayment();
        BigDecimal annualIncome = request.getAnnualIncome();

        // 본건 주담대 연간원리금상환액 계산
        RepaymentCalcServiceRequest repaymentCalcServiceRequest = RepaymentCalcServiceRequest.builder()
            .repaymentType(repaymentType)
            .principal(loanAmount.doubleValue())
            .term(loanTerm)
            .interestRate(interestRate.doubleValue())
            .build();

        RepaymentCalcResponse repaymentCalcResponse = repaymentCalcService.calculate(repaymentCalcServiceRequest);

        log.info("repaymentCalcResponse.getTotalPrincipal() : " + repaymentCalcResponse.getTotalPrincipal());
        log.info("repaymentCalcResponse.getTotalInterest() : " + repaymentCalcResponse.getTotalInterest());



        BigDecimal annualRepaymentPrincipal = BigDecimal.valueOf(repaymentCalcResponse.getTotalPrincipal() / loanTerm * 12);
        BigDecimal annualRepaymentInterest = BigDecimal.valueOf(repaymentCalcResponse.getTotalInterest() / loanTerm * 12);

        log.info("annualRepaymentPrincipal : " + annualRepaymentPrincipal);
        log.info("annualRepaymentInterest : " + annualRepaymentInterest);

        // DTI 계산
        BigDecimal annualRepaymentAmount = annualRepaymentPrincipal.add(annualRepaymentInterest).add(yearlyLoanInterestRepayment);
        BigDecimal dti = annualRepaymentAmount.divide(annualIncome, 4, RoundingMode.HALF_UP);

        return DtiCalcResponse.builder()
            .dtiRatio(dti)
            .annualIncome(annualIncome)
            .annualRepaymentAmount(annualRepaymentAmount)
            .annualRepaymentPrincipal(annualRepaymentPrincipal)
            .annualRepaymentInterest(annualRepaymentInterest)
            .yearlyLoanInterestRepayment(yearlyLoanInterestRepayment)
            .build();
    }
}
