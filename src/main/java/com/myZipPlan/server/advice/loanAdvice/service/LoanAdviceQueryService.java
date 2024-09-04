package com.myZipPlan.server.advice.loanAdvice.service;

import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProductFactory;
import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.exception.customException.AuthException;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class LoanAdviceQueryService {

    private final UserRepository userRepository;
    private final LoanAdviceResultRepository loanAdviceResultRepository;
    private final LoanProductFactory loanProductFactory;

    public List<LoanAdviceSummaryResponse> getRecentLoanAdvices() {
        User user = fetchCurrentUser();

        List<LoanAdviceResult> loanAdviceResults = loanAdviceResultRepository.findTop20ByUserIdOrderByCreatedDateTimeDesc(user.getId());

        if (loanAdviceResults.isEmpty()) {
            return null;
        }

        List<LoanAdviceSummaryResponse> response = loanAdviceResults.stream()
            .map(loanAdviceResult -> LoanAdviceSummaryResponse.builder()
                .loanAdviceResultId(loanAdviceResult.getId())
                .loanProductName(loanAdviceResult.getLoanProductName())
                .loanProductCode(loanAdviceResult.getLoanProductCode())
                .possibleLoanLimit(loanAdviceResult.getPossibleLoanLimit())
                .expectedLoanRate(loanAdviceResult.getExpectedLoanRate())
                .build())
            .toList();

        return response;
    }

    public LoanAdviceResponse getSpecificLoanAdvice(Long loanAdviceResultId) {

        LoanAdviceResult loanAdviceResult = loanAdviceResultRepository.findById(loanAdviceResultId)
            .orElseThrow(() -> new IllegalArgumentException("대출 상품 추천 결과가 없습니다."));

        List<Bank> availableBanks = getAvailableBanks(loanAdviceResult.getLoanProductCode());
        Long userInputInfoId = loanAdviceResult.getUserInputInfo().getId();
        return LoanAdviceResponse.of(loanAdviceResult, userInputInfoId, availableBanks);
    }


    private User fetchCurrentUser() {
        String providerId = SecurityUtils.getProviderId();

        User user = userRepository.findByOauthProviderId(providerId)
            .orElseThrow(() -> new AuthException("사용자 정보가 없습니다."));

        return user;
    }

    private List<Bank> getAvailableBanks(String productCode) {
        return loanProductFactory.getAvailableBanks(productCode);
    }
}
