package com.myZipPlan.server.advice.loanAdvice.model.loanProductImpl;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import com.myZipPlan.server.advice.rateProvider.service.RateProviderService;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Component
public class HfLeaseLoan implements LoanProduct {

    private final RateProviderService rateProviderService;

    private static final BigDecimal INCOME_LIMIT = new BigDecimal("130000000");
    private static final BigDecimal LOAN_LIMIT = new BigDecimal("300000000");



    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.HF_LEASE_LOAN;
    }

    @Override
    public LoanLimitAndRateResultDto calculateMaxLoanLimitAndMinRate(BigDecimal rentalAmount) {

        BigDecimal minRate = calculateMinRate(rentalAmount);

        return LoanLimitAndRateResultDto.builder()
            .productType(getProductType())
            .possibleLoanLimit(LOAN_LIMIT)
            .expectedLoanRate(minRate)
            .isEligible(true)
            .build();
    }

    @Override
    public FilterProductResultDto filtering(LoanAdviceServiceRequest request) {
        /*
          주신보전세대출
          검증 목록
          1. 보증금 - 수도권 7억 이하
          2. 보증금 - 수도권 외 5억 이하
          3. 무주택 혹은 1주택자
         */

        List<String> notEligibleReasons = new ArrayList<>();

        // 1. 임차보증금 수도권 7억 이하 여부
        if (isMetropolitanArea(request.getDistrictCode())
            && request.getRentalDeposit().compareTo(new BigDecimal("700000000")) > 0) {
            notEligibleReasons.add("수도권 임차보증금 7억 이하만 가능합니다.");
        }

        // 2. 임차보증금 비수도권 5억 이하 여부
        if (!isMetropolitanArea(request.getDistrictCode())
            && request.getRentalDeposit().compareTo(new BigDecimal("500000000")) > 0) {
            notEligibleReasons.add("비수도권 임차보증금 5억 이하만 가능합니다.");
        }

        // 3. 무주택 혹은 1주택자
        if (request.getHouseOwnershipType() == JeonseHouseOwnershipType.MULTI_HOUSE) {
            notEligibleReasons.add("무주택자 혹은 1주택자만 가능합니다.");
        }

        return FilterProductResultDto.builder()
            .productType(getProductType())
            .isEligible(notEligibleReasons.isEmpty())
            .notEligibleReasons(notEligibleReasons)
            .build();
    }


    // 기타비용산출(보증요율, 보증보험료 등)
    @Override
    public BigDecimal getGuaranteeInsuranceFee(BigDecimal loanAmount) {
        // 신한은행 홈피 기준 보증료 연 0.05% * 2년치
        return loanAmount.multiply(new BigDecimal("0.001"));
    }

    @Override
    public List<Bank> getAvailableBanks() {
        return List.of(Bank.HANA, Bank.SHINHAN, Bank.KB, Bank.WOORI, Bank.NH, Bank.BNK, Bank.KAKAO, Bank.TOSS);
    }

    @Override
    public LoanLimitAndRateResultDto calculateLoanLimitAndRate(LoanAdviceServiceRequest request) {
        // 한도산출
        BigDecimal possibleLoanLimit = calculateLoanLimit(request);
        // 금리산출
        BigDecimal finalRate = calculateFinalRate(request);

        return LoanLimitAndRateResultDto.builder()
            .productType(getProductType())
            .possibleLoanLimit(possibleLoanLimit)
            .expectedLoanRate(finalRate)
            .build();

    }


    private boolean isMetropolitanArea(String districtCode) {
        // 행정표준코드관리시스템 상으로 11 서울, 41 경기, 28 인천
        return districtCode.startsWith("11") || districtCode.startsWith("41") || districtCode.startsWith("28");
    }

    private BigDecimal calculateLoanLimit(LoanAdviceServiceRequest request) {
        BigDecimal rentalDeposit = request.getRentalDeposit();
        BigDecimal calculatedLimit = rentalDeposit.multiply(new BigDecimal("0.8"));

        BigDecimal combinedIncome = request.getAnnualIncome().add(request.getSpouseAnnualIncome());
        BigDecimal baseOnIncome = combinedIncome.multiply(new BigDecimal("4.5"));

        // 셋중 min 값을 반환
        return calculatedLimit.min(baseOnIncome).min(LOAN_LIMIT);
    }

    private BigDecimal calculateFinalRate(LoanAdviceServiceRequest request) {
        // 신한 홈피 기준
        return rateProviderService.getBaseRate(BaseRate.COFIX_NEW_BALANCE).add(new BigDecimal("1.61"));
    }

    private BigDecimal calculateMinRate(BigDecimal rentalDeposit) {
        return rateProviderService.getBaseRate(BaseRate.COFIX_NEW_BALANCE).add(new BigDecimal("1.61"));
    }

}
