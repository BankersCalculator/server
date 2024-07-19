package com.bankersCalculator.server.common.enums.ltv;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegionType {
    // TODO: 추후 지역별 소액임차보증금 세분화할 것
    SEOUL("서울", 50000000),
    CAPITAL_AREA("수도권(+세종시)", 43000000),
    METROPOLITAN_CITY("광역시", 23000000),
    OTHER_AREAS("기타", 20000000);

    private final String description;
    private final double smallAmountLeaseDeposit;

}
