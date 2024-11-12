package com.myZipPlan.server.advice.loanAdvice.model.loanProductImpl;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import com.myZipPlan.server.advice.rateProvider.service.RateProviderService;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.BaseRate;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseHouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Component
public class FixedRateLeaseLoan implements LoanProduct {

    private final RateProviderService rateProviderService;

    private static final BigDecimal LOAN_LIMIT = new BigDecimal("400000000");


    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.FIXED_RATE_LEASE_LOAN;
    }

    @Override
    public LoanLimitAndRateResultDto calculateMaxLoanLimitAndMinRate(BigDecimal rentalAmount) {

        BigDecimal minRate = calculateFinalRate();

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
           1. 무주택
           2. 수도권 보증금 7억 이하
           3. 지방 보증금 5억 이하
         */

        List<String> notEligibleReasons = new ArrayList<>();

        // 1. 무주택 여부
        if (request.getHouseOwnershipType() != JeonseHouseOwnershipType.NO_HOUSE) {
            notEligibleReasons.add("무주택자만 가능합니다.");
        }

        // 2. 임차보증금 수도권 7억 이하 여부
        if (isMetropolitanArea(request.getDistrictCode())
            && request.getRentalDeposit().compareTo(new BigDecimal("700000000")) > 0) {
            notEligibleReasons.add("수도권 임차보증금 7억 이하만 가능합니다.");
        }

        // 3. 임차보증금 비수도권 5억 이하 여부
        if (!isMetropolitanArea(request.getDistrictCode())
            && request.getRentalDeposit().compareTo(new BigDecimal("500000000")) > 0) {
            notEligibleReasons.add("비수도권 임차보증금 5억 이하만 가능합니다.");
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
        // 0.02% ~ 0.1% 로 나와 있음. 0.05 * 2 = 0.1%로 가정
        return loanAmount.multiply(new BigDecimal("0.001"));
    }

    // TODO: 경남은행, K뱅크, 기업은행 추가할 것
    @Override
    public List<Bank> getAvailableBanks() {
        return List.of(Bank.HANA);
    }

    @Override
    public List<String> getProductFeatures() {
        return List.of("고정금리", "최대4억", "금리상승시유리");
    }

    @Override
    public LoanLimitAndRateResultDto calculateLoanLimitAndRate(LoanAdviceServiceRequest request) {
        // 한도산출
        BigDecimal possibleLoanLimit = calculateLoanLimit(request);
        // 금리산출
        BigDecimal finalRate = calculateFinalRate();

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
        BigDecimal calculatedLimit = rentalDeposit.multiply(new BigDecimal("0.9"));

        BigDecimal combinedIncome = request.getAnnualIncome().add(request.getSpouseAnnualIncome());
        BigDecimal baseOnIncome = combinedIncome.multiply(new BigDecimal("5"));

        return calculatedLimit.min(baseOnIncome).min(LOAN_LIMIT);

    }

    private BigDecimal calculateFinalRate() {
        // 하나은행 홈피 기준. 금융채2년물 + 1.0%
        return rateProviderService.getBaseRate(BaseRate.FINANCIAL_BOND_24M).add(new BigDecimal("1.0"));
    }

}
