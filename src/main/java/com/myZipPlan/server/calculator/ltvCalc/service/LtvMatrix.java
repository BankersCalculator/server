package com.myZipPlan.server.calculator.ltvCalc.service;

import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.calculator.LoanPurpose;
import com.myZipPlan.server.common.enums.calculator.RegionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

@Component
public class LtvMatrix {

    private final Map<LoanPurpose, Map<RegionType, Map<HouseOwnershipType, BigDecimal>>> ltvMatrix;

    public LtvMatrix() {
        Map<LoanPurpose, Map<RegionType, Map<HouseOwnershipType, BigDecimal>>> matrix = new EnumMap<>(LoanPurpose.class);

        // HOME_PURCHASE
        matrix.put(LoanPurpose.HOME_PURCHASE, new EnumMap<>(RegionType.class));

        matrix.get(LoanPurpose.HOME_PURCHASE).put(RegionType.REGULATED_AREA, new EnumMap<>(Map.of(
            HouseOwnershipType.LIFETIME_FIRST, BigDecimal.valueOf(80),
            HouseOwnershipType.ORDINARY_DEMAND, BigDecimal.valueOf(70),
            HouseOwnershipType.NO_HOUSE, BigDecimal.valueOf(50),
            HouseOwnershipType.SINGLE_HOUSE_DISPOSAL, BigDecimal.valueOf(50),
            HouseOwnershipType.MORE_THAN_ONE_HOUSE, BigDecimal.valueOf(30)
        )));
        matrix.get(LoanPurpose.HOME_PURCHASE).put(RegionType.NON_REGULATED_CAPITAL_AREA, new EnumMap<>(Map.of(
            HouseOwnershipType.LIFETIME_FIRST, BigDecimal.valueOf(80),
            HouseOwnershipType.ORDINARY_DEMAND, BigDecimal.valueOf(70),
            HouseOwnershipType.NO_HOUSE, BigDecimal.valueOf(70),
            HouseOwnershipType.SINGLE_HOUSE_DISPOSAL, BigDecimal.valueOf(60),
            HouseOwnershipType.MORE_THAN_ONE_HOUSE, BigDecimal.valueOf(60)
        )));
        matrix.get(LoanPurpose.HOME_PURCHASE).put(RegionType.OTHER_AREAS, new EnumMap<>(Map.of(
            HouseOwnershipType.LIFETIME_FIRST, BigDecimal.valueOf(80),
            HouseOwnershipType.ORDINARY_DEMAND, BigDecimal.valueOf(70),
            HouseOwnershipType.NO_HOUSE, BigDecimal.valueOf(70),
            HouseOwnershipType.SINGLE_HOUSE_DISPOSAL, BigDecimal.valueOf(70),
            HouseOwnershipType.MORE_THAN_ONE_HOUSE, BigDecimal.valueOf(60)
        )));

        // LIVING_STABILITY
        matrix.put(LoanPurpose.LIVING_STABILITY, new EnumMap<>(RegionType.class));
        matrix.get(LoanPurpose.LIVING_STABILITY).put(RegionType.REGULATED_AREA, new EnumMap<>(Map.of(
            HouseOwnershipType.SINGLE_HOUSE, BigDecimal.valueOf(50),
            HouseOwnershipType.MORE_THAN_TWO_HOUSE, BigDecimal.valueOf(40)
        )));
        matrix.get(LoanPurpose.LIVING_STABILITY).put(RegionType.NON_REGULATED_CAPITAL_AREA, new EnumMap<>(Map.of(
            HouseOwnershipType.SINGLE_HOUSE, BigDecimal.valueOf(70),
            HouseOwnershipType.MORE_THAN_TWO_HOUSE, BigDecimal.valueOf(60)
        )));
        matrix.get(LoanPurpose.LIVING_STABILITY).put(RegionType.OTHER_AREAS, new EnumMap<>(Map.of(
            HouseOwnershipType.SINGLE_HOUSE, BigDecimal.valueOf(70),
            HouseOwnershipType.MORE_THAN_TWO_HOUSE, BigDecimal.valueOf(60)
        )));

        this.ltvMatrix = matrix;
    }

    public BigDecimal getLtvRatio(LoanPurpose loanPurpose, RegionType regionType, HouseOwnershipType houseOwnershipType) {
        return ltvMatrix.get(loanPurpose).get(regionType).get(houseOwnershipType);
    }
}