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
    private final LoanLimitCalculationService loanLimitCalculationService;
    private final ProductComparisonService productComparisonService;


    public LoanAdvise generateLoanAdvise(LoanAdviseServiceRequest request) {


        // TODO: List<LoanProduct> 부분 전부 전용 DTO로 변환할 것.. LoanProduct는 각 개별 서비스 안에서 사용하는 것으로 할 것임.
        // 대출 가능 상품을 필터링한다. 불가능한 상품은 filter 사유를 반환한다.
        List<LoanProduct> availableLoanProducts = productFilteringService.filterProduct(request);
        // 가능한 상품들 대상으로 한도산출을 진행한다. LoanProduct 는 새로운 DTO 로 변경할까?
        List<LoanProduct> loanProductsAfterLoanLimitCalc = loanLimitCalculationService.calculateLoanLimit(availableLoanProducts);
        // 가능 상품 중 추천 상품을 선정한다. 마찬가지로 DTO 변환..?
        List<LoanProduct> selectedLoanProducts = productComparisonService.compareProducts(loanProductsAfterLoanLimitCalc);

        /*
        AdditionalInformationService 와 ReportGenerationService 도 추가할 것.
        AdditionalInformationService 는 ReportGenerationService 의 하위 개념으로 보아도 괜찮을 거 같기도?
         */







        return null;

    }
}
