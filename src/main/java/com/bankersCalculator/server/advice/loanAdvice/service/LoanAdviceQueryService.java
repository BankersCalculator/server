package com.bankersCalculator.server.advice.loanAdvice.service;

import com.bankersCalculator.server.advice.loanAdvice.dto.internal.AdditionalInformation;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.OptimalLoanProductResult;
import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.RecommendedProductDto;
import com.bankersCalculator.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.bankersCalculator.server.advice.loanAdvice.repository.LoanAdviceResultRepository;
import com.bankersCalculator.server.advice.loanAdvice.service.component.*;
import com.bankersCalculator.server.advice.userInputInfo.domain.UserInputInfo;
import com.bankersCalculator.server.advice.userInputInfo.repository.UserInputInfoRepository;
import com.bankersCalculator.server.common.enums.loanAdvice.JeonseLoanProductType;
import com.bankersCalculator.server.oauth.userInfo.SecurityUtils;
import com.bankersCalculator.server.user.entity.User;
import com.bankersCalculator.server.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class LoanAdviceQueryService {

    private final UserRepository userRepository;

    public List<LoanAdviceSummaryResponse> getRecentLoanAdvices() {
        return null;
    }

    public LoanAdviceResponse getSpecificLoanAdvice(Long loanAdviceResultId) {
        return null;
    }

}
