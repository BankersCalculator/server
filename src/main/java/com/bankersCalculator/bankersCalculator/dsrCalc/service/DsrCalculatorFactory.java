package com.bankersCalculator.bankersCalculator.dsrCalc.service;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcRequest;
import org.springframework.stereotype.Component;

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

    public double calcTotalDsr(DsrCalcRequest request) {
        double totalDsrAmount = 0 ;
        int annualIncome = request.getAnnualIncome();
        for (DsrCalcRequest.LoanStatus loanStatus : request.getLoanStatusList()) {
            DsrCalculator calculator = calculators.get(loanStatus.getLoanType());
            if (calculator == null) {
                throw new RuntimeException("예외할것...");
            }
            totalDsrAmount += calculator.calculateDsr(loanStatus);
        }
        return (totalDsrAmount / annualIncome) * 100;
    }
}
