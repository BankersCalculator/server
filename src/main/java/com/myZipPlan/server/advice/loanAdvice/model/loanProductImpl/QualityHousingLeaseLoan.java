package com.myZipPlan.server.advice.loanAdvice.model.loanProductImpl;

import com.myZipPlan.server.advice.loanAdvice.dto.internal.FilterProductResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.internal.LoanTermsResultDto;
import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.model.LoanProduct;
import com.myZipPlan.server.advice.rateProvider.service.RateProviderService;
import com.myZipPlan.server.common.enums.Bank;
import com.myZipPlan.server.common.enums.loanAdvice.BaseRate;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseLoanProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Component
public class QualityHousingLeaseLoan implements LoanProduct {

    private final RateProviderService rateProviderService;

    private static final BigDecimal LOAN_LIMIT = new BigDecimal("500000000");


    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.QUALITY_HOUSING_LEASE_LOAN;
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


        List<String> notEligibleReasons = new ArrayList<>();

        return FilterProductResultDto.builder()
            .productType(getProductType())
            .isEligible(notEligibleReasons.isEmpty())
            .notEligibleReasons(notEligibleReasons)
            .build();
    }


    // 기타비용산출(보증요율, 보증보험료 등)
    @Override
    public BigDecimal getGuaranteeInsuranceFee(BigDecimal loanAmount) {
        // 질권설정통지비용
        return new BigDecimal(30000);
    }

    @Override
    public List<Bank> getAvailableBanks() {
        return List.of(Bank.HANA, Bank.KB, Bank.SHINHAN, Bank.WOORI, Bank.NH);
    }

    @Override
    public List<String> getProductFeatures() {
        return List.of("높은한도", "우량차주대상", "서울보증");
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

        BigDecimal rentalDeposit = request.getRentalDeposit();
        BigDecimal calculatedLimit = rentalDeposit.multiply(new BigDecimal("0.8"));

        return calculatedLimit.min(LOAN_LIMIT);

    }

    private BigDecimal calculateFinalRate() {
        // 국민은행 홈피 기준. 신잔액COFIX6개월 + 1.64%
        return rateProviderService.getBaseRate(BaseRate.COFIX_NEW_BALANCE).add(new BigDecimal("1.64"));
    }

}
