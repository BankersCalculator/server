package com.bankersCalculator.bankersCalculator.dsrCalc.service;

import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcResponse;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DsrCalcService {

    private final DsrCalculatorFactory dsrCalculatorFactory;

    public DsrCalcResponse dsrCalculate(DsrCalcServiceRequest request) {
        double totalDsrAmount = 0;
        int annualIncome = request.getAnnualIncome();
        int totalLoanCount = 0;

        List<DsrCalcResult> dsrCalcResultList = new ArrayList<>();
        for (DsrCalcServiceRequest.LoanStatus loanStatus : request.getLoanStatusList()) {
            DsrCalculator calculator = dsrCalculatorFactory.getCalculator(loanStatus.getLoanType());
            DsrCalcResult dsrCalcResult = calculator.calculateDsr(loanStatus);

            dsrCalcResult.setSerial(++totalLoanCount);
            totalDsrAmount += dsrCalcResult.getAnnualPrincipalRepayment();
            totalDsrAmount += dsrCalcResult.getAnnualInterestRepayment();

            dsrCalcResultList.add(dsrCalcResult);
        }

        double totalDsrRatio = (totalDsrAmount / annualIncome);

        return DsrCalcResponse.builder()
            .annualIncome(annualIncome)
            .totalLoanCount(totalLoanCount)
            .dsrCalcResultList(dsrCalcResultList)
            .finalDsrRatio(totalDsrRatio)
            .build();
    }
}
