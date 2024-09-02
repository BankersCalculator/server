package com.myZipPlan.server.advice.loanAdvice.service;

import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.myZipPlan.server.common.exception.customException.AuthException;
import com.myZipPlan.server.oauth.userInfo.SecurityUtils;
import com.myZipPlan.server.user.entity.User;
import com.myZipPlan.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class LoanAdviceQueryService {

    private final UserRepository userRepository;
    private final LoanAdviceResultRepository loanAdviceResultRepository;

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
        return null;
    }


    private User fetchCurrentUser() {
        String providerId = SecurityUtils.getProviderId();

        User user = userRepository.findByOauthProviderId(providerId)
            .orElseThrow(() -> new AuthException("사용자 정보가 없습니다."));

        return user;
    }
}
