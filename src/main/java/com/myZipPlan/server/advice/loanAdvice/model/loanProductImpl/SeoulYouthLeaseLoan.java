package com.myZipPlan.server.advice.loanAdvice.model.loanProductImpl;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import com.myZipPlan.server.advice.rateProvider.service.RateProviderService;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.BaseRate;
import com.myZipPlan.server.common.enums.loanAdvice.ChildStatus;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Component
public class SeoulYouthLeaseLoan implements LoanProduct {

    private final RateProviderService rateProviderService;

    private static final BigDecimal INCOME_LIMIT = new BigDecimal("40000000");
    private static final BigDecimal LOAN_LIMIT = new BigDecimal("200000000");



    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.SEOUL_YOUTH_LEASE_LOAN;
    }

    @Override
    public LoanLimitAndRateResultDto calculateMaxLoanLimitAndMinRate(BigDecimal rentalAmount) {

        BigDecimal minRate = calculateMinRate();

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
          서울시청년임차보증금대출
          검증 목록
          1. 주거지 서울 여부
          2. 만 39세 이하 무주택
          3. 임차보증금 3억 이하
          4. 월세 70만원 이하
          5. 연소득 4천만원 이하
         */

        List<String> notEligibleReasons = new ArrayList<>();

        // 1. 주거지 서울 여부. 법정동코드 11 - 서울시
        if (!request.getDistrictCode().startsWith("11")) {
            notEligibleReasons.add("서울시 거주자만 가능합니다.");
        }

        // 2. 만 39세 이하 무주택
        if (request.getAge() > 39) {
            notEligibleReasons.add("만 39세 이하만 가능합니다.");
        }


        // 3. 임차보증금 3억 이하
        if (request.getRentalDeposit().compareTo(new BigDecimal("300000000")) > 0) {
            notEligibleReasons.add("임차보증금 3억 이하만 가능합니다.");
        }

        // 4. 월세 70만원 이하
        if (request.getMonthlyRent().compareTo(new BigDecimal("700000")) > 0) {
            notEligibleReasons.add("월세 70만원 이하만 가능합니다.");
        }

        // 5. 연소득 4천만원 이하
        if (request.getAnnualIncome().compareTo(INCOME_LIMIT) > 0) {
            notEligibleReasons.add("연소득 4천만원 이하만 가능합니다.");
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
        return loanAmount.multiply(new BigDecimal("0.001"));
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
        BigDecimal finalRate = calculateFinalRate();

        return LoanLimitAndRateResultDto.builder()
            .productType(getProductType())
            .possibleLoanLimit(possibleLoanLimit)
            .expectedLoanRate(finalRate)
            .build();

    }

    private BigDecimal calculateLoanLimit(LoanAdviceServiceRequest request) {
        BigDecimal rentalDeposit = request.getRentalDeposit();
        BigDecimal calculatedLimit = rentalDeposit.multiply(new BigDecimal("0.9"));

        // min 값을 반환
        return calculatedLimit.min(LOAN_LIMIT);
    }

    private BigDecimal calculateFinalRate() {

        BigDecimal baseRate = rateProviderService.getBaseRate(BaseRate.COFIX_NEW_BALANCE); // 신잔액기준COFIX
        BigDecimal marginRate = new BigDecimal("1.45");
        BigDecimal discountRate = new BigDecimal("2.0");
        BigDecimal calculatedRate = baseRate.add(marginRate).subtract(discountRate);

        return calculatedRate.compareTo(BigDecimal.ONE) < 0 ? BigDecimal.ONE : calculatedRate;
    }

    private BigDecimal calculateMinRate() {
        BigDecimal baseRate = rateProviderService.getBaseRate(BaseRate.COFIX_NEW_BALANCE);
        BigDecimal marginRate = new BigDecimal("1.45");
        BigDecimal discountRate = new BigDecimal("2.0");
        BigDecimal calculatedRate = baseRate.add(marginRate).subtract(discountRate);

        return calculatedRate.compareTo(BigDecimal.ONE) < 0 ? BigDecimal.ONE : calculatedRate;
    }
}
