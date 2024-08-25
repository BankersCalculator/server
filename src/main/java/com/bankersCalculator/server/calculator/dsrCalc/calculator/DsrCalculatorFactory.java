package com.bankersCalculator.server.calculator.dsrCalc.calculator;

import com.bankersCalculator.server.calculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.server.common.enums.LoanType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.bankersCalculator.server.common.message.ExceptionMessage.NO_SUCH_CALCULATOR;

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
            throw new RuntimeException(NO_SUCH_CALCULATOR);
        }
        return calculator;
    }
}
