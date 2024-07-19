package com.bankersCalculator.server.ltvCalc.dto;

import com.bankersCalculator.server.common.enums.ltv.HouseOwnershipType;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.LoanPurpose;
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

    private double loanAmount;  // 대출금액
    private double collateralValue; // 담보가치
    private double priorMortgage;   // 선순위채권
    private int numbersOfRooms; // 방 수
    private HousingType housingType; // 주택유형
    private RegionType regionType; // 지역
    private double currentLeaseDeposit; // 현재임차보증금
//    private HouseOwnershipType houseOwnershipType;  // 주택보유유형
//    private LoanPurpose loanPurpose;    // 대출목적

}
