package com.bankersCalculator.bankersCalculator.dsrCalc.calculator;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcResponse;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class SecuritiesCollateralLoanDsrCalc implements DsrCalculator{

    @Override
    public DsrCalcResponse calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {
        return null;
    }


    @Override
    public LoanType getLoanType() {
        return LoanType.SECURITIES_COLLATERAL_LOAN;
    }
}
