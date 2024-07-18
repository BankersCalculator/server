package com.bankersCalculator.server.dsrCalc.service;

import com.bankersCalculator.server.common.enums.LoanType;
import com.bankersCalculator.server.dsrCalc.calculator.DsrCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DsrCalculatorFactory {

    private final Map<LoanType, DsrCalculator> calculators;

    public DsrCalculatorFactory(List<DsrCalculator> calculatorList) {
        calculators = new EnumMap<>(LoanType.class);
        for (DsrCalculator calculator : calculatorList) {
            calculators.put(calculator.getLoanType(), calculator);
        }
    }

    public DsrCalculator getCalculator(LoanType loanType) {
        DsrCalculator calculator = calculators.get(loanType);
        if (calculator == null) {
            throw new RuntimeException("예외처리할것..."); // TODO: exception 생성
        }
        return calculator;
    }
}
