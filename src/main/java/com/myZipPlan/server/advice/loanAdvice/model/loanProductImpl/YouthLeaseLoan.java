package com.myZipPlan.server.advice.loanAdvice.model.loanProductImpl;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import com.myZipPlan.server.advice.rateProvider.service.RateProviderService;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.BaseRate;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Component
public class YouthLeaseLoan implements LoanProduct {

    private final RateProviderService rateProviderService;

    private static final BigDecimal COMBINED_INCOME_LIMIT = new BigDecimal("70000000");
    private static final BigDecimal SINGLE_INCOME_LIMIT = new BigDecimal("70000000");
    private static final BigDecimal LOAN_LIMIT = new BigDecimal("200000000");


    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.YOUTH_LEASE_LOAN;
    }

    @Override
    public FilterProductResultDto filtering(LoanAdviceServiceRequest request) {

        /*
          1. 무주택 세대주
          2. 만 34세 이하
          3. 합산연소득 7천만원 이하
          4. 수도권 임차보증금 7억 이하
          5. 지방 임차보증금 5억 이하
         */

        List<String> notEligibleReasons = new ArrayList<>();

        // 1. 무주택 여부
        if (request.getHouseOwnershipType() != HouseOwnershipType.NO_HOUSE) {
            notEligibleReasons.add("무주택자만 가능합니다.");
        }

        // 2. 만 34세 이하 여부
        if (request.getAge() > 34) {
            notEligibleReasons.add("만 34세 이하만 가능합니다.");
        }

        // 3. 부부합산소득 7천만원 이하 여부
        if (request.getAnnualIncome().add(request.getSpouseAnnualIncome()).compareTo(COMBINED_INCOME_LIMIT) > 0) {
            notEligibleReasons.add("합산소득 7천만원 이하만 가능합니다.");
        }

        // 4. 임차보증금 수도권 7억 이하 여부
        if (isMetropolitanArea(request.getDistrictCode())
            && request.getRentalDeposit().compareTo(new BigDecimal("700000000")) > 0) {
            notEligibleReasons.add("수도권 임차보증금 7억 이하만 가능합니다.");
        }

        // 6. 임차보증금 비수도권 5억 이하 여부
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
        // TODO: 보증료 근거자료 찾을 수 없음.
        // 우선 임의로 최저보증료 0.02%로 설정 * 2년치
        return loanAmount.multiply(new BigDecimal("0.04"));
    }

    @Override
    public List<Bank> getAvailableBanks() {
        return List.of(Bank.HANA);
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
        BigDecimal calculatedLimit = rentalDeposit.multiply(new BigDecimal("0.9"));

        return calculatedLimit.compareTo(LOAN_LIMIT) > 0 ? LOAN_LIMIT : calculatedLimit;
    }

    private BigDecimal calculateFinalRate(LoanAdviceServiceRequest request) {
        // 하나은행 홈피 기준. 금융채6개월물 + 1.04%
        return rateProviderService.getBaseRate(BaseRate.FINANCIAL_BOND_6M).add(new BigDecimal("1.04"));
    }
}
