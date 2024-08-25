package com.bankersCalculator.server.advice.loanAdvice.dto.internal;

import com.bankersCalculator.server.common.enums.Bank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdditionalInformation {

    // 실 주거 비용
    private Long monthlyInterestCost;  // 월 이자 비용
    private Long monthlyRent;  // 월세
    private Long totalLivingCost;  // 총 주거 비용

    // 기회 비용
    private Long opportunityCostOwnFunds; // 기회비용
    private Double depositInterestRate; // 예금 이자율

    // 부수 비용
    private Long guaranteeInsuranceFee; // 보증보험료
    private Long stampDuty; // 인지세

    // 취급 가능 은행
    private List<Bank> availableBanks;

    // 전세 대출 가이드
    private String rentalLoanGuide;
}
