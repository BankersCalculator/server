package com.bankersCalculator.bankersCalculator.ltvCalc.dto;

import com.bankersCalculator.bankersCalculator.common.enums.ltv.HouseOwnershipType;
import com.bankersCalculator.bankersCalculator.common.enums.ltv.HousingType;
import com.bankersCalculator.bankersCalculator.common.enums.ltv.LoanPurpose;
import com.bankersCalculator.bankersCalculator.common.enums.ltv.RegionType;
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
    private HouseOwnershipType houseOwnershipType;
    private LoanPurpose loanPurpose;

}
