package com.myZipPlan.server.housingInfo.rentTransactionInquiry.common;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RentHousingType {
    APARTMENT("아파트")
   , OFFICETEL("오피스텔")
   , HOUSEHOLD_HOUSE("연립다세대")
   , FAMILY_HOUSE("단독/다가구");

    private final String description;


}
