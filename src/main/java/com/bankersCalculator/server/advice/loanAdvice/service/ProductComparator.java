package com.bankersCalculator.server.advice.loanAdvice.service;

import com.bankersCalculator.server.advice.loanAdvice.dto.service.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.OptimalLoanProductResult;
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
