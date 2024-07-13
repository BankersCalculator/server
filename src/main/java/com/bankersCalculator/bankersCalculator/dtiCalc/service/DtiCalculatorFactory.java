package com.bankersCalculator.bankersCalculator.dtiCalc.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dtiCalc.calculator.DtiCalculator;


@Component
public class DtiCalculatorFactory {
	
	private final Map<LoanType, DtiCalculator> calculators;
	
	public DtiCalculatorFactory(List<DtiCalculator> calculatorList) {
        calculators = new EnumMap<>(LoanType.class);
        for (DtiCalculator calculator : calculatorList) {
            calculators.put(calculator.getLoanType(), calculator);
        }
    }
	
	
	public DtiCalculator getCalculator(LoanType loanType) {
        DtiCalculator calculator = calculators.get(loanType);
        if (calculator == null) {
            throw new RuntimeException("예외처리할것..."); // TODO: exception 생성
        }
        return calculator;
    }

}

