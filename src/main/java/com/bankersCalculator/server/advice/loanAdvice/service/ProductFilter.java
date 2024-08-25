package com.bankersCalculator.server.advice.loanAdvice.service;

import com.bankersCalculator.server.advice.loanAdvice.dto.api.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
import com.bankersCalculator.server.advice.loanAdvice.repository.LoanProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductFilter {

    private final LoanProductRepository loanProductRepository;

    public List<FilterProductResultDto> filterProduct(LoanAdviceServiceRequest request) {

        // 전세대출 상품들을 불러와서 필터링을 수행. 값을 DTO에 저장하자
        List<LoanProduct> loanProducts = loanProductRepository.findAll();
        for (LoanProduct loanProduct : loanProducts) {
            loanProduct.filtering();
        }

        return null;
    }
}
