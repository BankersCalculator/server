package com.myZipPlan.server.advice.loanAdvice.model.loanProductImpl;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import com.myZipPlan.server.advice.rateProvider.service.RateProviderService;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.ChildStatus;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseHouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Component
public class NewbornSpecialLeaseLoan implements LoanProduct {

    private final RateProviderService rateProviderService;

    private static final BigDecimal INCOME_LIMIT = new BigDecimal("130000000");
    private static final BigDecimal LOAN_LIMIT = new BigDecimal("300000000");


    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.NEWBORN_SPECIAL_LEASE_LOAN;
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
        /**
         * 검증할 목록
         *
         * 1. 신생아 출생 여부
         * 2. 무주택 세대 여부
         * 3. 부부합산소득 1.3억 초과 여부
         * 4. 순자산 3.45억 초과 여부
         * 5. 임차전용면적 85제곱미터 이하 여부
         * 6. 임차보증금 수도권 5억 이하 여부
         * 7. 임차보증금 비수도권 4억 이하 여부
         */

        List<String> notEligibleReasons = new ArrayList<>();

        // 1. 신생아 출생 여부
        if (!request.getHasNewborn()) {
            notEligibleReasons.add("신생아 출생자만 가능합니다.");
        }

        // 2. 무주택 세대 여부
        if (request.getHouseOwnershipType() != JeonseHouseOwnershipType.NO_HOUSE) {
            notEligibleReasons.add("무주택자만 가능합니다.");
        }

        // 3. 부부합산소득 1.3억 초과 여부
        if (request.getAnnualIncome().add(request.getSpouseAnnualIncome()).compareTo(INCOME_LIMIT) > 0) {
            notEligibleReasons.add("부부합산소득 1.3억 이하만 가능합니다.");
        }

        // 4. 순자산 3.45억 초과 여부
        if (request.getIsNetAssetOver345M()) {
            notEligibleReasons.add("순자산 3.45억 이하만 가능합니다.");
        }

        // 5. 임차전용면적 85제곱미터 이하 여부
        if (request.getExclusiveArea().compareTo(BigDecimal.valueOf(85)) > 0) {
            notEligibleReasons.add("임차전용면적 85제곱미터 이하만 가능합니다.");
        }
        // 6. 임차보증금 수도권 5억 이하 여부
        if (isMetropolitanArea(request.getDistrictCode())
            && request.getRentalDeposit().compareTo(new BigDecimal("500000000")) > 0) {
            notEligibleReasons.add("수도권 임차보증금 5억 이하만 가능합니다.");
        }

        // 7. 임차보증금 비수도권 4억 이하 여부
        if (!isMetropolitanArea(request.getDistrictCode())
            && request.getRentalDeposit().compareTo(new BigDecimal("400000000")) > 0) {
            notEligibleReasons.add("비수도권 임차보증금 4억 이하만 가능합니다.");
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
        // 신한은행 홈피 기준 보증료 0.05% * 2년치
        return loanAmount.multiply(new BigDecimal("0.001"));
    }

    @Override
    public List<Bank> getAvailableBanks() {
        return List.of(Bank.HANA, Bank.SHINHAN, Bank.KB);
    }

    @Override
    public List<String> getProductFeatures() {
        return List.of("신생아특례", "저렴한금리", "최대3억");
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

        // HUG 가능하기 때문에 연소득환산한도는 산출안함.
        BigDecimal rentalDeposit = request.getRentalDeposit();
        BigDecimal calculatedLimit = rentalDeposit.multiply(new BigDecimal("0.8"));

        return calculatedLimit.min(LOAN_LIMIT);

    }

    private BigDecimal calculateFinalRate(LoanAdviceServiceRequest request) {
        BigDecimal combinedIncome = request.getAnnualIncome().add(request.getSpouseAnnualIncome());
        BigDecimal baseRate = rateProviderService.getNewBornSpecialLeaseLoanRate(request.getRentalDeposit(), combinedIncome);
        BigDecimal discountRate = calculateDiscountRate(request.getChildStatus(), request.getHasNewborn());
        BigDecimal calculatedRate = baseRate.subtract(discountRate);

        return calculatedRate.compareTo(BigDecimal.ONE) < 0 ? BigDecimal.ONE : calculatedRate;
    }

    private BigDecimal calculateMinRate(BigDecimal rentalDeposit) {
        BigDecimal baseRate = rateProviderService.getNewBornSpecialLeaseLoanRate(rentalDeposit, BigDecimal.valueOf(20000000));
        BigDecimal discountRate = calculateDiscountRate(ChildStatus.THREE_OR_MORE_CHILDREN, true);
        BigDecimal calculatedRate = baseRate.subtract(discountRate);

        return calculatedRate.compareTo(BigDecimal.ONE) < 0 ? BigDecimal.ONE : calculatedRate;
    }

    private BigDecimal calculateDiscountRate(ChildStatus childStatus, boolean hasNewborn ) {

        BigDecimal discountRate = BigDecimal.ZERO;


        /**
         * 2년 내 출생 자녀당 0.2% / 2년 초과 미성년 자녀당 0.1%
         * 인풋값에서 구분하기 어렵기 때문에 신생아 출생일 경우 0.1% 추가로 적용하는 것으로 갈음.
         */

        // 자녀당 0.1% 감면
        if (childStatus == ChildStatus.ONE_CHILD) {
            discountRate.add(new BigDecimal("0.1"));
        } else if (childStatus == ChildStatus.TWO_CHILD) {
            discountRate.add(new BigDecimal("0.2"));
        } else if (childStatus == ChildStatus.THREE_OR_MORE_CHILDREN) {
            discountRate.add(new BigDecimal("0.3"));
        }

        // 신생아 출생 시 0.1% 감면
        if (hasNewborn) {
            discountRate.add(new BigDecimal("0.1"));
        }

        return discountRate;
    }

    private boolean isEligibleMaritalStatus(MaritalStatus maritalStatus) {
        return maritalStatus == MaritalStatus.ENGAGED || maritalStatus == MaritalStatus.MARRIED;
    }
}
