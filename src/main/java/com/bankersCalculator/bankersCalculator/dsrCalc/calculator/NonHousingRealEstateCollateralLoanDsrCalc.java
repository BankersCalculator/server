package com.bankersCalculator.bankersCalculator.dsrCalc.calculator;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class NonHousingRealEstateCollateralLoanDsrCalc implements DsrCalculator{

    @Override
    public DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus) {
        return null;
    }

    @Override
    public LoanType getLoanType() {
        return LoanType.NON_HOUSING_REAL_ESTATE_COLLATERAL_LOAN;
    }
}
