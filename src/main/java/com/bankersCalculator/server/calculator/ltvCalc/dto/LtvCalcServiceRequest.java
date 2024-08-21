package com.bankersCalculator.server.calculator.ltvCalc.dto;

import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LtvCalcServiceRequest {

    private Double loanAmount;  // 대출금액
    private Double collateralValue; // 담보가치
    private Double priorMortgage;   // 선순위채권
    private Integer numberOfRooms; // 방 수
    private HousingType housingType; // 주택유형
    private RegionType regionType; // 지역
    private Double currentLeaseDeposit; // 현재임차보증금
//    private HouseOwnershipType houseOwnershipType;  // 주택보유유형
//    private LoanPurpose loanPurpose;    // 대출목적


    public LtvCalcResponse toResponse(double topPriorityRepaymentAmount, double totalLoanExposure, double ltvRatio) {
        return LtvCalcResponse.builder()
            .loanAmount(loanAmount)
            .collateralValue(collateralValue)
            .priorMortgage(priorMortgage)
            .numbersOfRooms(numberOfRooms)
            .smallAmountLeaseDeposit(regionType.getSmallAmountLeaseDeposit())
            .topPriorityRepaymentAmount(topPriorityRepaymentAmount)
            .totalLoanExposure(totalLoanExposure)
            .ltvRatio(ltvRatio)
            .build();
    }

}
