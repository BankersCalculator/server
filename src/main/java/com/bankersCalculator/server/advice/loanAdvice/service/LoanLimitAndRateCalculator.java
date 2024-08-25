package com.bankersCalculator.server.advice.loanAdvice.service;

import com.bankersCalculator.server.advice.loanAdvice.dto.api.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
import com.bankersCalculator.server.common.enums.JeonseLoanProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class LoanLimitAndRateCalculator {

    private final Map<JeonseLoanProductType, LoanProduct> loanProducts;

    @Autowired
    public LoanLimitAndRateCalculator(List<LoanProduct> products) {
        this.loanProducts = products.stream()
            .collect(Collectors.toMap(LoanProduct::getProductType, Function.identity()));
    }

    public List<LoanLimitAndRateResultDto> calculateLoanLimitAndRate(LoanAdviceServiceRequest request,
                                                                     List<FilterProductResultDto> filteredProducts) {

        List<LoanLimitAndRateResultDto> result = new ArrayList<>();

        for (FilterProductResultDto filteredProduct : filteredProducts) {
            if (filteredProduct.isEligible()) {
                LoanProduct loanProduct = loanProducts.get(filteredProduct.getProductType());
                if (loanProduct != null) {
                    LoanLimitAndRateResultDto loanLimitAndRateResult = loanProduct.calculateLoanLimitAndRate(request);

                    result.add(loanLimitAndRateResult);
                }
            }
        }

        return result;
    }
}
