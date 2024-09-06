package com.myZipPlan.server.advice.loanAdvice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleLoanAdviceRequest {
    @NotNull(message = "임차보증금은 필수 입력값입니다.")
    @Min(value = 1000000, message = "임차보증금은 1백만원 이상이어야 합니다.")
    private BigDecimal rentalDeposit; // 임차보증금
}