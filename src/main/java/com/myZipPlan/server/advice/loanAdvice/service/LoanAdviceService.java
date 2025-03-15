package com.myZipPlan.server.advice.loanAdvice.service;

import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.PreLoanTermsResponse;
import com.myZipPlan.server.advice.loanAdvice.service.component.*;
import com.myZipPlan.server.common.enums.RoleType;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import com.myZipPlan.server.user.userService.GuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class LoanAdviceService {
    // Facade 패턴으로 변경

    private final LoanAdviceOrchestrator loanAdviceOrchestrator;
    private final LoanTermCalculator loanTermCalculator;
    private final GuestService guestService;

    // 전체 대출추천 요청 처리
    public LoanAdviceResponse createLoanAdvice(LoanAdviceServiceRequest request) {
        checkGuestUsage();
        return loanAdviceOrchestrator.processLoanAdvice(request);
    }

    // 특정 상품 요청 처리
    public LoanAdviceResponse generateLoanAdviceOnSpecificLoan(Long userInputInfoId, String productCode) {
        checkGuestUsage();
        return loanAdviceOrchestrator.processSpecificLoanAdvice(userInputInfoId, productCode);
    }

    public PreLoanTermsResponse preCalculateLoanTerms(LoanAdviceServiceRequest request) {
        return loanAdviceOrchestrator.preCalculateLoanTerms(request);
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

    private void checkGuestUsage() {
        if (RoleType.GUEST == SecurityUtils.getRoleType()) {
            guestService.canUseGuestFeature(SecurityUtils.getProviderId());
        }
    }
}
