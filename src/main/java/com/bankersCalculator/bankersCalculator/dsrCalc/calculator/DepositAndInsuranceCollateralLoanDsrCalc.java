package com.bankersCalculator.bankersCalculator.dsrCalc.calculator;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepositAndInsuranceCollateralLoanDsrCalc implements DsrCalculator{

    @Override
    public double calculateDsr(DsrCalcRequest.LoanStatus loanStatus) {
        return 0;
    }

    @Override
    public LoanType getLoanType() {
        return LoanType.DEPOSIT_AND_INSURANCE_COLLATERAL_LOAN;
    }
}
