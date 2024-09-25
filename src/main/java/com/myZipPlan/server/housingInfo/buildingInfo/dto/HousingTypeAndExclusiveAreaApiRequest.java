package com.myZipPlan.server.housingInfo.buildingInfo.dto;

import lombok.Getter;

@Getter
public class HousingTypeAndExclusiveAreaApiRequest {
    private String districtCodeFirst5;
    private String districtCodeLast5;
    private String jibunMain;
    private String jibunSub;
}
