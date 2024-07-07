package com.bankersCalculator.bankersCalculator.dsrCalc.calculator;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.domain.DsrCalcResult;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;

public interface DsrCalculator {

    DsrCalcResult calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus);

    LoanType getLoanType();
}
