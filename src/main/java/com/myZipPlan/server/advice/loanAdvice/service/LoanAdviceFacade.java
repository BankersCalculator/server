package com.myZipPlan.server.advice.loanAdvice.service;

import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.advice.loanAdvice.service.component.LoanTermCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class LoanAdviceFacade {
    private final LoanAdviceOrchestrator loanAdviceOrchestrator;
    private final LoanTermCalculator loanTermCalculator;

    // 전체 대출추천 요청 처리
    public LoanAdviceResponse createLoanAdvice(LoanAdviceServiceRequest request) {
        return loanAdviceOrchestrator.processLoanAdvice(request);
    }

    // 특정 상품 요청 처리
    public LoanAdviceResponse generateLoanAdviceOnSpecificLoan(Long userInputInfoId, String productCode) {
        return loanAdviceOrchestrator.processSpecificLoanAdvice(userInputInfoId, productCode);
    }

    // 간편조회: 최대한도/최저금리 리스트 반환
    public List<LoanAdviceSummaryResponse> getSimpleLoanConditions(BigDecimal rentalDeposit) {
        return loanTermCalculator.calculateSimpleLoanTerms(rentalDeposit).stream()
            .map(dto -> LoanAdviceSummaryResponse.builder()
                .loanProductName(dto.getProductType().getProductName())
                .loanProductCode(dto.getProductType().getProductCode())
                .possibleLoanLimit(dto.getPossibleLoanLimit())
                .expectedLoanRate(dto.getExpectedLoanRate())
                .build())
            .collect(Collectors.toList());
    }
}
