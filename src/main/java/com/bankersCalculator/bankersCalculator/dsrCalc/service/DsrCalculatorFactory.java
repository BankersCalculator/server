package com.bankersCalculator.bankersCalculator.dsrCalc.service;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcResponse;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class DsrCalculatorFactory {

    private final Map<LoanType, DsrCalculator> calculators;

    public DsrCalculatorFactory(List<DsrCalculator> calculatorList) {
        calculators = new EnumMap<>(LoanType.class);
        for (DsrCalculator calculator : calculatorList) {
            calculators.put(calculator.getLoanType(), calculator);
        }
    }

    public DsrCalcResponse calcTotalDsr(DsrCalcServiceRequest request) {
        double totalDsrAmount = 0;
        int annualIncome = request.getAnnualIncome();
        int totalLoanCount = 0;

        List<DsrCalcResult> dsrCalcResultList = new ArrayList<>();

        for (DsrCalcServiceRequest.LoanStatus loanStatus : request.getLoanStatusList()) {
            DsrCalculator calculator = calculators.get(loanStatus.getLoanType());
            if (calculator == null) {
                throw new RuntimeException("예외처리할것..."); // TODO: exception 생성
            }
            DsrCalcResult dsrCalcResult = calculator.calculateDsr(loanStatus);
            dsrCalcResult.setSerial(++totalLoanCount);
            totalDsrAmount += dsrCalcResult.getAnnualPrincipalRepayment();
            totalDsrAmount += dsrCalcResult.getAnnualInterestRepayment();
        }

        double totalDsrRatio = (totalDsrAmount / annualIncome) * 100;

        return DsrCalcResponse.builder()
            .annualIncome(annualIncome)
            .totalLoanCount(totalLoanCount)
            .dsrCalcResultList(dsrCalcResultList)
            .finalDsrRatio(totalDsrRatio)
            .build();
    }
}
