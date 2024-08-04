package com.bankersCalculator.server.advise.jeonseLoanAdvise.service;

import com.bankersCalculator.server.advise.jeonseLoanAdvise.domain.LoanProduct;
import com.bankersCalculator.server.advise.jeonseLoanAdvise.dto.LoanAdviseServiceRequest;
import com.bankersCalculator.server.advise.jeonseLoanAdvise.repository.LoanProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LoanLimitCalculationService {

    private final LoanProductRepository loanProductRepository;

    public List<LoanProduct> calculateLoanLimit(List<LoanProduct> productList) {

        List<LoanProduct> loanProducts = loanProductRepository.findAll();
        for (LoanProduct loanProduct : loanProducts) {
            loanProduct.calculateLoanLimit();
        }

        return null;
    }
}
