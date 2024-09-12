package com.myZipPlan.server.advice.loanAdvice.model.loanProductImpl;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import com.myZipPlan.server.advice.rateProvider.service.RateProviderService;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Component
public class SmeYouthMonthlyRentLoan implements LoanProduct {

    private final RateProviderService rateProviderService;

    private static final BigDecimal COMBINED_INCOME_LIMIT = new BigDecimal("50000000");
    private static final BigDecimal SINGLE_INCOME_LIMIT = new BigDecimal("50000000");
    private static final BigDecimal LOAN_LIMIT = new BigDecimal("100000000");


    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.SME_YOUTH_MONTHLY_RENT_LOAN;
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
          1. 무주택 여부
          2. 부부합산소득 5천만원 이하 여부
          3. 단독세대주 소득 35백만원 이하 여부
          4. 만 34세 이하 여부
          5. 중소기업 재직여부
          6. 자산 3.45억 초과 여부
          7. 임차전용면적 85제곱미터 이하 여부
          8. 임차보증금 2억 이하 여부
         */

        List<String> notEligibleReasons = new ArrayList<>();

        // 1. 무주택 여부
        if (request.getHouseOwnershipType() != HouseOwnershipType.NO_HOUSE) {
            notEligibleReasons.add("무주택자만 가능합니다.");
        }


        // 2. 부부합산소득 5천만원 이하 여부
        if (request.getAnnualIncome().add(request.getSpouseAnnualIncome()).compareTo(COMBINED_INCOME_LIMIT) > 0) {
            notEligibleReasons.add("부부합산소득 5천만원 이하만 가능합니다.");
        }

        // 3. 단독세대주 소득 35백만원 이하 여부
        if (request.getMaritalStatus() == MaritalStatus.SINGLE
            && request.getAnnualIncome().compareTo(SINGLE_INCOME_LIMIT) > 0) {
            notEligibleReasons.add("단독세대주 소득 35백만원 이하만 가능합니다.");
        }

        // 4. 만 34세 이하 여부
        if (request.getAge() > 34) {
            notEligibleReasons.add("만 34세 이하만 가능합니다.");
        }

        // 5. 중소기업 재직여부
        if (!request.getIsSMEEmployee()) {
            notEligibleReasons.add("중소기업 재직자만 가능합니다.");
        }

        // 6. 자산 3.45억 초과 여부
        if (request.getIsNetAssetOver345M()) {
            notEligibleReasons.add("자산 3.45억 초과시 대출 불가능합니다.");
        }

        // 7. 임차전용면적 85제곱미터 이하 여부
        if (request.getExclusiveArea().compareTo(new BigDecimal("85")) > 0) {
            notEligibleReasons.add("임차전용면적 85제곱미터 이하만 가능합니다.");
        }

        // 8. 임차보증금 2억 이하 여부
        if (request.getRentalDeposit().compareTo(new BigDecimal("200000000")) > 0) {
            notEligibleReasons.add("임차보증금 2억 이하만 가능합니다.");
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
        // TODO: HUG 반환보증일 경우 0.31% 추가. 어떻게 안내할 것인지 고민
        // 하나은행 홈피 기준 보증료 연 0.154% * 2년치
        return loanAmount.multiply(new BigDecimal("0.00308"));
    }

    @Override
    public List<Bank> getAvailableBanks() {
        return List.of(Bank.HANA, Bank.SHINHAN, Bank.KB, Bank.WOORI, Bank.NH, Bank.IM, Bank.BNK);
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

    private BigDecimal calculateLoanLimit(LoanAdviceServiceRequest request) {
        BigDecimal rentalDeposit = request.getRentalDeposit();
        BigDecimal calculatedLimit = rentalDeposit.multiply(new BigDecimal("0.8"));

        return calculatedLimit.compareTo(LOAN_LIMIT) > 0 ? LOAN_LIMIT : calculatedLimit;
    }

    private BigDecimal calculateFinalRate() {
        return new BigDecimal("1.5");
    }
}
