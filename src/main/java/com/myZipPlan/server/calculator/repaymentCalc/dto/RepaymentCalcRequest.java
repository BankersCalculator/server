package com.myZipPlan.server.calculator.repaymentCalc.dto;

import com.myZipPlan.server.common.enums.calculator.RepaymentType;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.myZipPlan.server.common.message.ValidationMessage.*;

@Getter
@Setter
@NoArgsConstructor
public class RepaymentCalcRequest {


    @NotNull(message = NOT_NULL_REPAYMENT_TYPE)
    private RepaymentType repaymentType;

    @NotNull(message = NOT_NULL_PRINCIPAL)
    @Min(value = 1000000, message = MIN_VALUE_PRINCIPAL)
    @Max(value = 10000000000L, message = MAX_VALUE_PRINCIPAL)
    private Double principal; // 원금

    private Double maturityPaymentAmount; // 만기상환금액

    @NotNull(message = NOT_NULL_INTEREST_RATE_PERCENTAGE)
    @DecimalMin(value = "0.0", inclusive = false, message = MIN_VALUE_INTEREST_RATE_PERCENTAGE)
    @DecimalMax(value = "20.0", message = MAX_VALUE_INTEREST_RATE_PERCENTAGE)
    private Double interestRatePercentage; // 연이자율

    @NotNull(message = NOT_NULL_TERM)
    @Min(value = 1, message = MIN_VALUE_TERM)
    @Max(value = 600, message = MAX_VALUE_TERM)
    private Integer term; // 기간(개월수)

    @Max(value = 600, message = MAX_VALUE_GRACE_PERIOD)
    private Integer gracePeriod; // 거치기간


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
