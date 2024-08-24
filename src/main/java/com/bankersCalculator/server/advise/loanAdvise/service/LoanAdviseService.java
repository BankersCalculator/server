package com.bankersCalculator.server.advise.loanAdvise.service;

import com.bankersCalculator.server.advise.loanAdvise.dto.*;
import com.bankersCalculator.server.advise.userInputInfo.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advise.userInputInfo.dto.UserInputInfoServiceRequest;
import com.bankersCalculator.server.advise.loanAdvise.model.LoanProduct;
import com.bankersCalculator.server.common.enums.Bank;
import com.bankersCalculator.server.common.enums.loanAdvise.AreaSize;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.RentalType;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import com.bankersCalculator.server.housingInfo.buildingInfo.common.RentHousingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class LoanAdviseService {

    private final ProductFilter productFilter;
    private final LoanLimitCalculator loanLimitCalculator;
    private final ProductComparator productComparator;

    public LoanAdviseResponse generateLoanAdvise(LoanAdviseServiceRequest request) {


        // TODO: List<LoanProduct> 부분 전부 전용 DTO로 변환할 것.. LoanProduct는 각 개별 서비스 안에서 사용하는 것으로 할 것임.
        // 대출 가능 상품을 필터링한다. 불가능한 상품은 filter 사유를 반환한다.
        List<LoanProduct> availableLoanProducts = productFilter.filterProduct(request);
        // 가능한 상품들 대상으로 한도산출을 진행한다. LoanProduct 는 새로운 DTO 로 변경할까?
        List<LoanProduct> loanProductsAfterLoanLimitCalc = loanLimitCalculator.calculateLoanLimit(availableLoanProducts);
        // 가능 상품 중 추천 상품을 선정한다. 마찬가지로 DTO 변환..?
        List<LoanProduct> selectedLoanProducts = productComparator.compareProducts(loanProductsAfterLoanLimitCalc);

        /*
        AdditionalInformationService 와 ReportGenerationService 도 추가할 것.
        AdditionalInformationService 는 ReportGenerationService 의 하위 개념으로 보아도 괜찮을 거 같기도?
         */
        return LoanAdviseResponse.builder()
            .loanAdviseResultId(1L)
            .loanProductName("샘플 전세자금대출")
            .loanProductCode("SAMPLE001")
            .possibleLoanLimit(200000000.0)
            .expectedLoanRate(3.5)
            .totalRentalDeposit(300000000L)
            .loanAmount(200000000L)
            .ownFunds(100000000L)
            .monthlyInterestCost(583333L)
            .monthlyRent(0L)
            .totalLivingCost(583333L)
            .opportunityCostOwnFunds(100000000L)
            .depositInterestRate(2.5)
            .calculatedCost(2500000L)
            .guaranteeInsuranceFee(1000000L)
            .stampDuty(150000L)
            .recommendationReason("고객님의 소득과 신용도를 고려하여 가장 적합한 상품으로 선정되었습니다.")
            .recommendedProducts(Arrays.asList(
                RecommendedProductDto.builder()
                    .rank(2)
                    .loanProductName("다른 은행 전세자금대출")
                    .loanProductCode("OTHER001")
                    .possibleLoanLimit(180000000.0)
                    .expectedLoanRate(3.7)
                    .notEligibleReason(null)
                    .build(),
                RecommendedProductDto.builder()
                    .rank(3)
                    .loanProductName("보증금 반환 보증 전세자금대출")
                    .loanProductCode("GUARANTEE001")
                    .possibleLoanLimit(220000000.0)
                    .expectedLoanRate(3.8)
                    .notEligibleReason("보증금 반환 보증 가입 필요")
                    .build()
            ))
            .availableBanks(Arrays.asList(Bank.KOOMIN, Bank.SHINHAN, Bank.WOORI))
            .rentalLoanGuide("전세자금대출 이용 시 주의사항:\n1. 대출 기간 동안 이자를 꾸준히 납부해야 합니다.\n2. 전세 계약 만료 시 대출금 상환 계획을 미리 세워야 합니다.")
            .build();
    }

    public LoanAdviseResponse generateLoanAdviseOnSpecificLoan(String productCode, Long userId, Long adviseResultId) {
        return LoanAdviseResponse.builder()
            .loanAdviseResultId(1L)
            .loanProductName("샘플 전세자금대출")
            .loanProductCode("SAMPLE001")
            .possibleLoanLimit(200000000.0)
            .expectedLoanRate(3.5)
            .totalRentalDeposit(300000000L)
            .loanAmount(200000000L)
            .ownFunds(100000000L)
            .monthlyInterestCost(583333L)
            .monthlyRent(0L)
            .totalLivingCost(583333L)
            .opportunityCostOwnFunds(100000000L)
            .depositInterestRate(2.5)
            .calculatedCost(2500000L)
            .guaranteeInsuranceFee(1000000L)
            .stampDuty(150000L)
            .recommendationReason("고객님의 소득과 신용도를 고려하여 가장 적합한 상품으로 선정되었습니다.")
            .recommendedProducts(Arrays.asList(
                RecommendedProductDto.builder()
                    .rank(2)
                    .loanProductName("다른 은행 전세자금대출")
                    .loanProductCode("OTHER001")
                    .possibleLoanLimit(180000000.0)
                    .expectedLoanRate(3.7)
                    .notEligibleReason(null)
                    .build(),
                RecommendedProductDto.builder()
                    .rank(3)
                    .loanProductName("보증금 반환 보증 전세자금대출")
                    .loanProductCode("GUARANTEE001")
                    .possibleLoanLimit(220000000.0)
                    .expectedLoanRate(3.8)
                    .notEligibleReason("보증금 반환 보증 가입 필요")
                    .build()
            ))
            .availableBanks(Arrays.asList(Bank.KOOMIN, Bank.SHINHAN, Bank.WOORI))
            .rentalLoanGuide("전세자금대출 이용 시 주의사항:\n1. 대출 기간 동안 이자를 꾸준히 납부해야 합니다.\n2. 전세 계약 만료 시 대출금 상환 계획을 미리 세워야 합니다.")
            .build();
    }
}
