package com.bankersCalculator.server.repaymentCalc.dto;

import com.bankersCalculator.server.common.enums.RepaymentType;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepaymentCalcRequest {


    @NotNull(message = "상환 유형은 필수입니다.")
    private RepaymentType repaymentType;

    @NotNull(message = "원금은 필수입니다.")
    @Min(value = 1000000, message = "원금은 1백만원 이상이어야 합니다.")
    @Max(value = 10000000000L, message = "원금은  100억원을 초과할 수 없습니다.")
    private double principal; // 원금

    @NotNull(message = "기간은 필수입니다.")
    @Min(value = 1, message = "기간은 1개월 이상이어야 합니다.")
    @Max(value = 600, message = "기간은 600개월을 초과할 수 없습니다.")
    private int term; // 기간(개월수)

    @Max(value = 600, message = "거치기간은 600개월을 초과할 수 없습니다.")
    private int gracePeriod; // 거치기간

    @NotNull(message = "연이자율은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "연이자율은 0보다 커야 합니다.")
    @DecimalMax(value = "20.0", message = "연이자율은 20%를 초과할 수 없습니다.")
    private double interestRatePercentage; // 연이자율

    private double maturityPaymentAmount; // 만기상환금액

    @Builder
    private RepaymentCalcRequest(RepaymentType repaymentType, double principal, int term, int gracePeriod, double interestRatePercentage, double maturityPaymentAmount) {
        this.repaymentType = repaymentType;
        this.principal = principal;
        this.term = term;
        this.gracePeriod = gracePeriod;
        this.interestRatePercentage = interestRatePercentage;
        this.maturityPaymentAmount = maturityPaymentAmount;
    }

    public RepaymentCalcServiceRequest toServiceRequest() {
        return RepaymentCalcServiceRequest.builder()
            .repaymentType(repaymentType)
            .principal(principal)
            .term(term)
            .gracePeriod(gracePeriod)
            .interestRate(interestRatePercentage / 100)
            .maturityPaymentAmount(maturityPaymentAmount)
            .build();
    }
}
