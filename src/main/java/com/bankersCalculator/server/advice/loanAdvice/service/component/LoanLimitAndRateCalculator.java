package com.bankersCalculator.server.advice.loanAdvice.service.component;

import com.bankersCalculator.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProductFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class LoanLimitAndRateCalculator {

    private final LoanProductFactory loanProductFactory;

    public List<LoanLimitAndRateResultDto> calculateLoanLimitAndRate(LoanAdviceServiceRequest request,
                                                                     List<FilterProductResultDto> filteredProducts) {

        List<LoanLimitAndRateResultDto> result = new ArrayList<>();

        for (FilterProductResultDto filteredProduct : filteredProducts) {
            LoanProduct loanProduct = loanProductFactory.getLoanProduct(filteredProduct.getProductType());
            LoanLimitAndRateResultDto loanLimitAndRateResult = loanProduct.calculateLoanLimitAndRate(request);
            result.add(loanLimitAndRateResult);
        }

        return result;
    }
}
