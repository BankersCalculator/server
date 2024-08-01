package com.bankersCalculator.server.advise.jeonseLoanAdvise.service;

import com.bankersCalculator.server.advise.jeonseLoanAdvise.domain.LoanAdvise;
import com.bankersCalculator.server.advise.jeonseLoanAdvise.domain.LoanProduct;
import com.bankersCalculator.server.advise.jeonseLoanAdvise.dto.LoanAdviseServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class JeonseLoanAdviseService {

    private final ProductFilteringService productFilteringService;


    public LoanAdvise generateLoanAdvise(LoanAdviseServiceRequest request) {
        List<LoanProduct> loanProducts = productFilteringService.filterProduct();



        return null;

    }
}
