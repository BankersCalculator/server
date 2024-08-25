package com.bankersCalculator.server.advice.loanAdvice.model.loanProductImpl;

import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.dto.internal.LoanLimitAndRateResultDto;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
import com.bankersCalculator.server.common.enums.JeonseLoanProductType;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.common.enums.ltv.HouseOwnershipType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Component
public class SeoulNewlyWedLeaseDepositLoan implements LoanProduct {

    private static final BigDecimal INCOME_LIMIT = new BigDecimal("130000000");
    private static final BigDecimal LOAN_LIMIT = new BigDecimal("300000000");



    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.SEOUL_NEWLYWED_LEASE_DEPOSIT;
    }

    @Override
    public String getProperty() {
        return null;
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
        BigDecimal rentalDeposit = request.getRentalDeposit();
        BigDecimal calculatedLimit = rentalDeposit.multiply(new BigDecimal("0.9"));
        BigDecimal possibleLoanLimit = calculatedLimit.compareTo(LOAN_LIMIT) > 0 ? LOAN_LIMIT : calculatedLimit;

        // 금리산출
        BigDecimal combinedIncome = request.getAnnualIncome().add(request.getSpouseAnnualIncome());

        BigDecimal baseRate = new BigDecimal("3.00"); // TODO: 기준금리 조회하는 공통모듈 개발할 것.
        BigDecimal marginRate = new BigDecimal("1.45");
        BigDecimal discountRate = calculateDiscountRate(combinedIncome);
        BigDecimal finalRate = baseRate.add(marginRate).subtract(discountRate);

        return LoanLimitAndRateResultDto.builder()
            .productType(getProductType())
            .possibleLoanLimit(possibleLoanLimit)
            .expectedLoanRate(finalRate)
            .build();

    }

    // 기타비용산출(보증요율, 인지세, 보증보험료 등)

    private BigDecimal calculateDiscountRate(BigDecimal combinedIncome) {
        if (combinedIncome.compareTo(new BigDecimal("30000000")) <= 0) {
            return new BigDecimal("3.0");
        } else if (combinedIncome.compareTo(new BigDecimal("60000000")) <= 0) {
            return new BigDecimal("2.5");
        } else if (combinedIncome.compareTo(new BigDecimal("90000000")) <= 0) {
            return new BigDecimal("2.0");
        } else if (combinedIncome.compareTo(new BigDecimal("110000000")) <= 0) {
            return new BigDecimal("1.5");
        } else if (combinedIncome.compareTo(new BigDecimal("130000000")) <= 0) {
            return new BigDecimal("1.0");
        } else {
            return BigDecimal.ZERO; // 1억 3천만원 초과시 감면 없음
        }
    }



    private boolean isEligibleMaritalStatus(MaritalStatus maritalStatus) {
        return maritalStatus == MaritalStatus.ENGAGED || maritalStatus == MaritalStatus.MARRIED;
    }
}
