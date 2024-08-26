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
import com.bankersCalculator.server.advice.loanAdvice.service.component.*;
import com.bankersCalculator.server.common.enums.Bank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class LoanAdviceService {

    private final ProductFilter productFilter;
    private final LoanLimitAndRateCalculator loanLimitAndRateCalculator;
    private final ProductComparator productComparator;
    private final AdditionalInfoGenerator additionalInfoGenerator;
    private final AiReportGenerator aiReportGenerator;


    @Transactional  // TODO: 확인할 것.
    public LoanAdviceResponse generateLoanAdvice(LoanAdviceServiceRequest request) {

        // 대출상품 필터링
        List<FilterProductResultDto> filterResults = productFilter.filterProduct(request);

        // 대출한도 및 금리 계산
        List<LoanLimitAndRateResultDto> loanLimitAndRateResultDto = loanLimitAndRateCalculator.calculateLoanLimitAndRate(request, filterResults);

        // 대출상품 비교
        BigDecimal rentalDeposit = request.getRentalDeposit();
        OptimalLoanProductResult optimalLoanProduct = productComparator.compareProducts(rentalDeposit, loanLimitAndRateResultDto);

        // 추가정보 생성
        AdditionalInformation additionalInformation = additionalInfoGenerator.generateAdditionalInfo(request, optimalLoanProduct);

        // 보고서 생성
        String aiReport = aiReportGenerator.generateAiReport();




        // UserInputInfo 저장
        // LoanAdviceResult 저장

        return null;
    }


    public LoanAdviceResponse generateLoanAdviceOnSpecificLoan(Long loanAdviceResultId, String productCode) {
        return null;
    }

    public List<LoanAdviceSummaryResponse> getRecentLoanAdvices() {
        return null;
    }

    public LoanAdviceResponse getSpecificLoanAdvice(Long loanAdviceResultId) {
        return null;
    }
}
