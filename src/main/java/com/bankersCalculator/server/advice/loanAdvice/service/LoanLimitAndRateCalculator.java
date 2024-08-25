package com.bankersCalculator.server.advice.loanAdvice.service;

import com.bankersCalculator.server.advice.loanAdvice.dto.api.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.LoanLimitAndRateResult;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
import com.bankersCalculator.server.advice.loanAdvice.repository.LoanProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Component
public class LoanLimitAndRateCalculator {

    private final LoanProductRepository loanProductRepository;

    public List<LoanLimitAndRateResult> calculateLoanLimitAndRate(LoanAdviceServiceRequest request,
                                                                  List<FilterProductResultDto> filteredProducts) {

        List<LoanProduct> loanProducts = loanProductRepository.findAll();
        for (LoanProduct loanProduct : loanProducts) {
            loanProduct.calculateLoanLimit();
        }

        return null;
    }
}
