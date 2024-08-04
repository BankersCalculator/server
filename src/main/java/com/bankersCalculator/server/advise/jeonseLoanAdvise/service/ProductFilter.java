package com.bankersCalculator.server.advise.jeonseLoanAdvise.service;

import com.bankersCalculator.server.advise.jeonseLoanAdvise.domain.LoanProduct;
import com.bankersCalculator.server.advise.jeonseLoanAdvise.dto.LoanAdviseServiceRequest;
import com.bankersCalculator.server.advise.jeonseLoanAdvise.repository.LoanProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductFilter {

    private final LoanProductRepository loanProductRepository;

    public List<LoanProduct> filterProduct(LoanAdviseServiceRequest request) {
        // filter를 위한 DTO는 따로 지정하는 게 나을 듯?

        // 전세대출 상품들을 불러와서 필터링을 수행. 값을 DTO에 저장하자
        List<LoanProduct> loanProducts = loanProductRepository.findAll();
        for (LoanProduct loanProduct : loanProducts) {
            loanProduct.filtering();
        }

        return null;
    }
}
