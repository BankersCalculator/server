package com.bankersCalculator.bankersCalculator.common.enums.ltv;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegionType {
    // TODO: LTV 지역 파악하고 수정할 것
    SEOUL("서울"),
    CAPITAL_AREA("수도권(+세종시)"),
    METROPOLITAN_CITY("광역시"),
    OTHER_AREAS("기타");

    private final String description;

}
