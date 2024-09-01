package com.myZipPlan.server.calculator.ltvCalc.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LtvCalcResponse {
    private Double loanAmount;
    private Double collateralValue;
    private Double priorMortgage;
    private Integer numbersOfRooms;
    private Double smallAmountLeaseDeposit;
    private Double topPriorityRepaymentAmount;
    private Double totalLoanExposure;
    private Double ltvRatio;
}
