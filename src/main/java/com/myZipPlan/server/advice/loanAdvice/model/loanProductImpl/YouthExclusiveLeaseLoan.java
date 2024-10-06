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
public class YouthExclusiveLeaseLoan implements LoanProduct {

    private final RateProviderService rateProviderService;

    private static final BigDecimal COMBINED_INCOME_LIMIT = new BigDecimal("75000000");
    private static final BigDecimal SINGLE_INCOME_LIMIT = new BigDecimal("50000000");
    private static final BigDecimal LOAN_LIMIT = new BigDecimal("200000000");


    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.YOUTH_EXCLUSIVE_LEASE_LOAN;
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
            1. 만 34세 이하
            2. 무주택자
            3. 부부합산소득 5천만원 이하
            4. 본인소득 5천만원 이하
            5. 순자산 3.45억 이하
            6. 임차전용면저 85m2 이하
            7. 임차보증금 3억 이하
         */

        List<String> notEligibleReasons = new ArrayList<>();

        // 1. 만 34세 이하
        if (request.getAge() > 34) {
            notEligibleReasons.add("만 34세 이하가 아닙니다.");
        }

        // 2. 무주택자
        if (request.getHouseOwnershipType() != HouseOwnershipType.NO_HOUSE) {
            notEligibleReasons.add("무주택자만 가능합니다.");
        }

        // 3. 부부합산소득 7500만원 이하
        if (request.getAnnualIncome().add(request.getSpouseAnnualIncome()).compareTo(COMBINED_INCOME_LIMIT) > 0) {
            notEligibleReasons.add("합산소득 7500만원 이하만 가능합니다.");
        }

        // 4. 본인소득 5천만원 이하
        if (request.getAnnualIncome().compareTo(SINGLE_INCOME_LIMIT) > 0) {
            notEligibleReasons.add("본인소득 5천만원 이하만 가능합니다.");
        }

        // 5. 순자산 3.45억 이하
        if (request.getIsNetAssetOver345M()) {
            notEligibleReasons.add("순자산 3.45억 이하만 가능합니다.");
        }

        // 6. 임차전용면적 85m2 이하
        if (request.getExclusiveArea().compareTo(new BigDecimal("85")) > 0) {
            notEligibleReasons.add("임차전용면적 85m2 이하만 가능합니다.");
        }

        // 7. 임차보증금 3억 이하
        if (request.getRentalDeposit().compareTo(new BigDecimal("300000000")) > 0) {
            notEligibleReasons.add("임차보증금 3억 이하만 가능합니다.");
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
        // TODO: HF, HUG 보증료가 다름
        return loanAmount.multiply(new BigDecimal("0.003"));
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
        BigDecimal finalRate = calculateFinalRate(request);

        return LoanLimitAndRateResultDto.builder()
            .productType(getProductType())
            .possibleLoanLimit(possibleLoanLimit)
            .expectedLoanRate(finalRate)
            .build();

    }

    private BigDecimal calculateLoanLimit(LoanAdviceServiceRequest request) {
        BigDecimal rentalDeposit = request.getRentalDeposit();
        BigDecimal calculatedLimit = rentalDeposit.multiply(new BigDecimal("0.8"));

        return calculatedLimit.min(LOAN_LIMIT);
    }

    private BigDecimal calculateFinalRate(LoanAdviceServiceRequest request) {
        BigDecimal annualIncome = request.getAnnualIncome();
        BigDecimal spouseAnnualIncome = request.getSpouseAnnualIncome();
        BigDecimal combinedIncome = annualIncome.add(spouseAnnualIncome);

        if (combinedIncome.compareTo(new BigDecimal("20000000")) <= 0) {
            return new BigDecimal("2.0");
        } else if (combinedIncome.compareTo(new BigDecimal("40000000")) <= 0) {
            return new BigDecimal("2.3");
        } else if (combinedIncome.compareTo(new BigDecimal("60000000")) <= 0) {
            return new BigDecimal("2.7");
        } else {
            return new BigDecimal("3.1");
        }
    }

    private BigDecimal calculateMinRate() {
        // 각종 우대금리 다 충족될 경우 최저금리
        return BigDecimal.valueOf(1.0);
    }
}
