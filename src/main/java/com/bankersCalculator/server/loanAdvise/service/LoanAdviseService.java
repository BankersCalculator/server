package com.bankersCalculator.server.loanAdvise.service;

import com.bankersCalculator.server.loanAdvise.domain.LoanAdvise;
import com.bankersCalculator.server.loanAdvise.domain.LoanProduct;
import com.bankersCalculator.server.loanAdvise.dto.LoanAdviseServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class LoanAdviseService {

    private final ProductFilteringService productFilteringService;


    public LoanAdvise generateLoanAdvise(LoanAdviseServiceRequest request) {
        List<LoanProduct> loanProducts = productFilteringService.filterProduct();



        return null;

    }
}
