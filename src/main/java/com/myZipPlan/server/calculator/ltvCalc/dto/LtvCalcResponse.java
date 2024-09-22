package com.myZipPlan.server.calculator.ltvCalc.dto;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class LtvCalcResponse {

    private BigDecimal ltvRatio;
    private BigDecimal collateralValue;
    private BigDecimal possibleLoanAmount;
}
