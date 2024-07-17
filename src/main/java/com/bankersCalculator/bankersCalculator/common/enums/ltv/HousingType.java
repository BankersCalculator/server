package com.bankersCalculator.bankersCalculator.common.enums.ltv;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HousingType {
    APARTMENT("아파트"),
    DETACHED_HOUSE("단독주택"),
    MULTI_FAMILY_HOUSE("다가구주택"),
    MULTI_HOUSEHOLD_HOUSE("다세대주택"),
    OFFICETEL("오피스텔"),
    OTHER("기타");

    private final String description;
}
