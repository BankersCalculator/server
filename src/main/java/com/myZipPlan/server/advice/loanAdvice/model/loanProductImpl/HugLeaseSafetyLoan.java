package com.myZipPlan.server.advice.loanAdvice.model.loanProductImpl;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanTermsResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import com.myZipPlan.server.advice.rateProvider.service.RateProviderService;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.loanAdvice.BaseRate;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import com.myZipPlan.server.housingInfo.buildingInfo.common.RentHousingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Component
public class HugLeaseSafetyLoan implements LoanProduct {

    private final RateProviderService rateProviderService;

    private static final BigDecimal LOAN_LIMIT = new BigDecimal("450000000");


    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.HUG_LEASE_SAFETY_LOAN;
    }

    @Override
    public LoanTermsResultDto calculateMaxLoanLimitAndMinRate(BigDecimal rentalAmount) {

        BigDecimal minRate = calculateFinalRate();

        return LoanTermsResultDto.builder()
            .productType(getProductType())
            .possibleLoanLimit(LOAN_LIMIT)
            .expectedLoanRate(minRate)
            .isEligible(true)
            .build();
    }

    @Override
    public FilterProductResultDto filtering(LoanAdviceServiceRequest request) {

        /*
          전세금안심대출
          검증 목록
          1. 보증금 - 수도권 7억 이하
          2. 보증금 - 수도권 외 5억 이하
          3. 목적물 - 아파트, 오피스텔
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

        // 3. 목적물 - 아파트, 오피스텔
        if (!request.getRentHousingType().equals(RentHousingType.APARTMENT)
            && !request.getRentHousingType().equals(RentHousingType.OFFICETEL)) {
            notEligibleReasons.add("목적물은 아파트, 오피스텔만 가능합니다.");
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
        // KB 홈피 기준. 0.154%(보증료) + 0.031%(반환보증) * 2년
        return loanAmount.multiply(new BigDecimal("0.00370"));
    }

    @Override
    public List<Bank> getAvailableBanks() {
        return List.of(Bank.HANA, Bank.KB, Bank.SHINHAN, Bank.WOORI, Bank.NH);
    }

    @Override
    public List<String> getProductFeatures() {
        return List.of("안심전세", "보증보험", "까다로운심사");
    }
    @Override
    public LoanTermsResultDto calculateLoanTerms(LoanAdviceServiceRequest request) {
        // 한도산출
        BigDecimal possibleLoanLimit = calculateLoanLimit(request);
        // 금리산출
        BigDecimal finalRate = calculateFinalRate();

        return LoanTermsResultDto.builder()
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

        BigDecimal sumIncome = request.getSumIncome();
        MaritalStatus maritalStatus = request.getMaritalStatus();
        boolean isNewlyMarried = maritalStatus.equals(MaritalStatus.NEWLY_MARRIED) || maritalStatus.equals(MaritalStatus.ENGAGED);
        boolean isYoung = request.getAge() <= 34;

        BigDecimal rate = new BigDecimal("0.8");

        if (sumIncome.compareTo(new BigDecimal("60000000")) <= 0
            && isNewlyMarried) {
            rate = new BigDecimal("0.9");
        }

        if (isYoung && sumIncome.compareTo(new BigDecimal("50000000")) <= 0) {
            rate = new BigDecimal("0.9");
        }

        BigDecimal rentalDeposit = request.getRentalDeposit();
        BigDecimal calculatedLimit = rentalDeposit.multiply(rate);

        return calculatedLimit.min(LOAN_LIMIT);

    }

    private BigDecimal calculateFinalRate() {
        // 국민은행 홈피 기준. 신잔액COFIX6개월 + 1.64%
        return rateProviderService.getBaseRate(BaseRate.COFIX_NEW_BALANCE).add(new BigDecimal("1.34"));
    }

}
