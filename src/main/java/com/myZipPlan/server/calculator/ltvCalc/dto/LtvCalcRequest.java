package com.myZipPlan.server.calculator.ltvCalc.dto;

import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.calculator.HousingType;
import com.myZipPlan.server.common.enums.calculator.LoanPurpose;
import com.myZipPlan.server.common.enums.calculator.RegionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LtvCalcRequest {

    private LoanPurpose loanPurpose;    // 대출목적
    private BigDecimal collateralValue; // 담보가치
    private RegionType regionType; // 지역
    private HouseOwnershipType houseOwnershipType;  // 주택보유유형


    public LtvCalcServiceRequest toServiceRequest() {
        return LtvCalcServiceRequest.builder()
            .loanPurpose(loanPurpose)
            .collateralValue(collateralValue)
            .regionType(regionType)
            .houseOwnershipType(houseOwnershipType)
            .build();
    }
}
