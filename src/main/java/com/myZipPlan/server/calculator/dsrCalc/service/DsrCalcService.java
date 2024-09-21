package com.myZipPlan.server.calculator.dsrCalc.service;

import com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.myZipPlan.server.calculator.dsrCalc.calculator.DsrCalculatorFactory;
import com.myZipPlan.server.calculator.dsrCalc.domain.DsrCalcResult;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcResponse;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DsrCalcService {

    private final DsrCalculatorFactory dsrCalculatorFactory;

    public DsrCalcResponse dsrCalculate(DsrCalcServiceRequest request) {
        BigDecimal totalDsrAmount = BigDecimal.ZERO;
        BigDecimal annualIncome = request.getAnnualIncome();
        BigDecimal totalLoanCount = BigDecimal.ZERO;

        List<DsrCalcResult> dsrCalcResultList = new ArrayList<>();
        for (DsrCalcServiceRequest.LoanStatus loanStatus : request.getLoanStatusList()) {
            DsrCalculator calculator = dsrCalculatorFactory.getCalculator(loanStatus.getLoanType());
            DsrCalcResult dsrCalcResult = calculator.calculateDsr(loanStatus);

            totalLoanCount = totalLoanCount.add(BigDecimal.ONE);
            dsrCalcResult.setSerial(totalLoanCount);
            totalDsrAmount = totalDsrAmount.add(dsrCalcResult.getAnnualPrincipalRepayment());
            totalDsrAmount = totalDsrAmount.add(dsrCalcResult.getAnnualInterestRepayment());

            dsrCalcResultList.add(dsrCalcResult);
        }

        BigDecimal totalDsrRatio = (totalDsrAmount.divide(annualIncome, 4, RoundingMode.DOWN));

        return DsrCalcResponse.builder()
            .annualIncome(annualIncome)
            .totalLoanCount(totalLoanCount)
            .dsrCalcResults(dsrCalcResultList)
            .finalDsrRatio(totalDsrRatio)
            .build();
    }
}
