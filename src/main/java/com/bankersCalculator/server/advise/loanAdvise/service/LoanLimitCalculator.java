package com.bankersCalculator.server.advise.loanAdvise.service;

import com.bankersCalculator.server.advise.loanAdvise.model.LoanProduct;
import com.bankersCalculator.server.advise.loanAdvise.repository.LoanProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LoanLimitCalculator {

    private final LoanProductRepository loanProductRepository;

    public List<LoanProduct> calculateLoanLimit(List<LoanProduct> productList) {

        List<LoanProduct> loanProducts = loanProductRepository.findAll();
        for (LoanProduct loanProduct : loanProducts) {
            loanProduct.calculateLoanLimit();
        }

        return null;
    }
}
