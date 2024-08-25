package com.bankersCalculator.server.advice.loanAdvice.service.component;

import com.bankersCalculator.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.OptimalLoanProductResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductComparator {

    public OptimalLoanProductResult compareProducts(List<LoanLimitAndRateResultDto> loanLimitAndRateResultDtos) {

        return null;
    }
}
