package com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculatorImpl;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.calculator.DsrCalculator;
import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class OtherCollateralLoanDsrCalc implements DsrCalculator {

    @Override
    public DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {
        return null;
    }

    @Override
    public LoanType getLoanType() {
        return LoanType.OTHER_COLLATERAL_LOAN;
    }
}
