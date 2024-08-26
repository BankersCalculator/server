package com.bankersCalculator.server.advice.loanAdvice.dto.internal;

import com.bankersCalculator.server.common.enums.Bank;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class AdditionalInformation {

    // 실주거 비용
    private BigDecimal ownFunds;  // 소요 자기 자금
    private BigDecimal monthlyInterestCost;  // 월 이자 비용
    private BigDecimal totalLivingCost;  // 총 주거 비용
    private BigDecimal monthlyRent;  // 월 임대료

    // 기회 비용
    private BigDecimal opportunityCostOwnFunds; // 기회비용
    private BigDecimal depositInterestRate; // 예금 이자율

    // 부수 비용
    private BigDecimal guaranteeInsuranceFee; // 보증보험료
    private BigDecimal stampDuty; // 인지세

    // 취급 가능 은행
    private List<Bank> availableBanks;

    // 전세 대출 가이드
    private String rentalLoanGuide;
}
