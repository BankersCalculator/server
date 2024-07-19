package com.bankersCalculator.server.ltvCalc.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LtvCalcResponse {
    private double loanAmount;
    private double collateralValue;
    private double priorMortgage;
    private int numbersOfRooms;
    private double smallAmountLeaseDeposit;
    private double topPriorityRepaymentAmount;
    private double totalLoanExposure;
    private double ltvRatio;
}
