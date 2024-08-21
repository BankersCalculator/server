package com.bankersCalculator.server.advice.loanAdvice.model.loanProductImpl;

import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
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

    // 이자율 산출


    // 기타비용산출(보증요율, 인지세, 보증보험료 등)


}
