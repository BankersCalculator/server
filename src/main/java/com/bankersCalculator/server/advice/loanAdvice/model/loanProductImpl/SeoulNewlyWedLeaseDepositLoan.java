package com.bankersCalculator.server.advice.loanAdvice.model.loanProductImpl;

import com.bankersCalculator.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
import com.bankersCalculator.server.advice.rateProvider.service.RateProviderService;
import com.bankersCalculator.server.common.enums.Bank;
import com.bankersCalculator.server.common.enums.loanAdvice.BaseRate;
import com.bankersCalculator.server.common.enums.loanAdvice.JeonseLoanProductType;
import com.bankersCalculator.server.common.enums.loanAdvice.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvice.MaritalStatus;
import com.bankersCalculator.server.common.enums.calculator.HouseOwnershipType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@RequiredArgsConstructor
@Component
public class SeoulNewlyWedLeaseDepositLoan implements LoanProduct {

    private final RateProviderService rateProviderService;

    private static final BigDecimal INCOME_LIMIT = new BigDecimal("130000000");
    private static final BigDecimal LOAN_LIMIT = new BigDecimal("300000000");



    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.SEOUL_NEWLYWED_LEASE_DEPOSIT;
    }

    @Override
    public FilterProductResultDto filtering(LoanAdviceServiceRequest request) {
        /*
          서울시신혼부부임차보증금대출
          검증할 목록
          1. 주거지 서울 여부
          2. 신혼부부 여부
          3. 부부합산소득 1.3억 초과 여부
          4. 무주택자 여부
          5. 임차보증금 7억 이하 여부
         */

        List<String> notEligibleReasons = new ArrayList<>();

        // 1. 주거지 서울 여부. 법정동코드 11 - 서울시
        if (!request.getDistrictCode().startsWith("11")) {
            notEligibleReasons.add("서울시 거주자만 가능합니다.");
        }

        // 2. 신혼부부 여부
        if (!isEligibleMaritalStatus(request.getMaritalStatus())) {
            notEligibleReasons.add("신혼부부만 가능합니다.");
        }

        // 3. 부부합산소득 1.3억 초과 여부
        BigDecimal combinedIncome = request.getAnnualIncome().add(request.getSpouseAnnualIncome());
        if (combinedIncome.compareTo(INCOME_LIMIT) > 0) {
            notEligibleReasons.add("부부합산소득 1.3억 초과시 대출 불가능합니다.");
        }

        // 4. 무주택자 여부
        if (request.getHouseOwnershipType() != HouseOwnershipType.NO_HOUSE) {
            notEligibleReasons.add("무주택자만 가능합니다.");
        }

        // 5. 임차보증금 7억 이하 여부
        if (request.getRentalDeposit().compareTo(new BigDecimal("700000000")) > 0) {
            notEligibleReasons.add("임차보증금 7억 이하만 가능합니다.");
        }

        return FilterProductResultDto.builder()
            .productType(getProductType())
            .isEligible(notEligibleReasons.isEmpty())
            .notEligibleReasons(notEligibleReasons)
            .build();
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

    // 기타비용산출(보증요율, 인지세, 보증보험료 등)

    @Override
    public BigDecimal getGuaranteeInsuranceFee(BigDecimal loanAmount) {
        // 신한은행 홈피 기준 보증료 0.05% * 2년치
        return loanAmount.multiply(new BigDecimal("0.001"));
    }

    @Override
    public List<Bank> getAvailableBanks() {
        return List.of(Bank.HANA, Bank.SHINHAN, Bank.KOOMIN);
    }

    private BigDecimal calculateLoanLimit(LoanAdviceServiceRequest request) {
        BigDecimal rentalDeposit = request.getRentalDeposit();
        BigDecimal calculatedLimit = rentalDeposit.multiply(new BigDecimal("0.9"));

        return calculatedLimit.compareTo(LOAN_LIMIT) > 0 ? LOAN_LIMIT : calculatedLimit;

    }

    private BigDecimal calculateFinalRate(LoanAdviceServiceRequest request) {
        BigDecimal combinedIncome = request.getAnnualIncome().add(request.getSpouseAnnualIncome());


        BigDecimal baseRate = rateProviderService.getBaseRate(BaseRate.COFIX_NEW_BALANCE); // 신잔액기준COFIX
        BigDecimal marginRate = new BigDecimal("1.45");
        BigDecimal discountRate = calculateDiscountRate(combinedIncome, request.getMaritalStatus(), request.getChildStatus());
        BigDecimal finalRate = baseRate.add(marginRate).subtract(discountRate);
        if (finalRate.compareTo(BigDecimal.ZERO) < 0) {
            finalRate = BigDecimal.ZERO;
        }

        return finalRate;
    }

    private BigDecimal calculateDiscountRate(BigDecimal combinedIncome, MaritalStatus maritalStatus, ChildStatus childStatus) {

        BigDecimal discountRate = BigDecimal.ZERO;

        // 소득에 따른 감면
        if (combinedIncome.compareTo(new BigDecimal("30000000")) <= 0) {
            discountRate.add(new BigDecimal("3.0"));
        } else if (combinedIncome.compareTo(new BigDecimal("60000000")) <= 0) {
            discountRate.add(new BigDecimal("2.5"));
        } else if (combinedIncome.compareTo(new BigDecimal("90000000")) <= 0) {
            discountRate.add(new BigDecimal("2."));
        } else if (combinedIncome.compareTo(new BigDecimal("110000000")) <= 0) {
            discountRate.add(new BigDecimal("1.5"));
        } else if (combinedIncome.compareTo(new BigDecimal("130000000")) <= 0) {
            discountRate.add(new BigDecimal("1.0"));
        }

        // 자녀당 0.5% 감면
        if (childStatus == ChildStatus.ONE_CHILD) {
            discountRate.add(new BigDecimal("0.5"));
        } else if (childStatus == ChildStatus.TWO_CHILD) {
            discountRate.add(new BigDecimal("1.0"));
        } else if (childStatus == ChildStatus.THREE_OR_MORE_CHILDREN) {
            discountRate.add(new BigDecimal("1.5"));
        }

        // 결혼예정자 0.2% 추가 감면
        if (maritalStatus == MaritalStatus.ENGAGED) {
            discountRate.add(new BigDecimal("0.2"));
        }

        return discountRate;
    }

    private boolean isEligibleMaritalStatus(MaritalStatus maritalStatus) {
        return maritalStatus == MaritalStatus.ENGAGED || maritalStatus == MaritalStatus.MARRIED;
    }
}
