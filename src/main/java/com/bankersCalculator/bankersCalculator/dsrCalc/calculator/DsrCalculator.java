package com.bankersCalculator.bankersCalculator.dsrCalc.calculator;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcResponse;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcServiceRequest;

public interface DsrCalculator {

    DsrCalcResponse calculateDsr(DsrCalcServiceRequest.LoanStatus loanStatus);

    LoanType getLoanType();
}
