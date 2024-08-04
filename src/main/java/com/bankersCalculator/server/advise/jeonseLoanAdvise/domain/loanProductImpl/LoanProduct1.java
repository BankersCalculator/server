package com.bankersCalculator.server.advise.jeonseLoanAdvise.domain.loanProductImpl;

import com.bankersCalculator.server.advise.jeonseLoanAdvise.domain.LoanProduct;
import org.springframework.stereotype.Component;

@Component
public class LoanProduct1 implements LoanProduct {

    @Override
    public String getProperty() {
        return null;
    }

    @Override
    public boolean filtering() {
        return false;
    }

    @Override
    public double calculateLoanLimit() {
        return 0;
    }
}
