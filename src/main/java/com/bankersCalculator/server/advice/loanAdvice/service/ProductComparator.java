package com.bankersCalculator.server.advice.loanAdvice.service;

import com.bankersCalculator.server.advice.loanAdvice.dto.service.LoanLimitAndRateResult;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.OptimalLoanProductResult;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductComparator {

    public OptimalLoanProductResult compareProducts(List<LoanLimitAndRateResult> loanLimitAndRateResults) {

        return null;
    }
}
