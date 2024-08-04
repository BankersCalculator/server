package com.bankersCalculator.server.advise.loanAdvise.service;

import com.bankersCalculator.server.advise.loanAdvise.domain.LoanAdviseResult;
import com.bankersCalculator.server.advise.loanAdvise.model.LoanProduct;
import com.bankersCalculator.server.advise.loanAdvise.dto.LoanAdviseServiceRequest;
import com.bankersCalculator.server.advise.loanAdvise.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advise.loanAdvise.dto.UserInputInfoServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class LoanAdviseService {

    private final ProductFilter productFilter;
    private final LoanLimitCalculator loanLimitCalculator;
    private final ProductComparator productComparator;


    public UserInputInfoResponse getSubmittedUserInput(UserInputInfoServiceRequest serviceRequest) {
        return null;
    }

    public LoanAdviseResult generateLoanAdvise(LoanAdviseServiceRequest request) {

        // TODO: List<LoanProduct> 부분 전부 전용 DTO로 변환할 것.. LoanProduct는 각 개별 서비스 안에서 사용하는 것으로 할 것임.
        // 대출 가능 상품을 필터링한다. 불가능한 상품은 filter 사유를 반환한다.
        List<LoanProduct> availableLoanProducts = productFilter.filterProduct(request);
        // 가능한 상품들 대상으로 한도산출을 진행한다. LoanProduct 는 새로운 DTO 로 변경할까?
        List<LoanProduct> loanProductsAfterLoanLimitCalc = loanLimitCalculator.calculateLoanLimit(availableLoanProducts);
        // 가능 상품 중 추천 상품을 선정한다. 마찬가지로 DTO 변환..?
        List<LoanProduct> selectedLoanProducts = productComparator.compareProducts(loanProductsAfterLoanLimitCalc);

        /*
        AdditionalInformationService 와 ReportGenerationService 도 추가할 것.
        AdditionalInformationService 는 ReportGenerationService 의 하위 개념으로 보아도 괜찮을 거 같기도?
         */

        return null;

    }

    public LoanAdviseResult generateLoanAdviseOnSpecificLoan(LoanAdviseServiceRequest serviceRequest) {
        return null;
    }
}
