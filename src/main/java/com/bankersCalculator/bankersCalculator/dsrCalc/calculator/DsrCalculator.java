package com.bankersCalculator.bankersCalculator.dsrCalc.calculator;

import com.bankersCalculator.bankersCalculator.common.enums.LoanType;
import com.bankersCalculator.bankersCalculator.dsrCalc.dto.DsrCalcRequest;

import java.util.List;

public interface DsrCalculator {

    double calculateDsr(DsrCalcRequest.LoanStatus loanStatus);

    LoanType getLoanType();
}
