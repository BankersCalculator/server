package com.bankersCalculator.server.housingInfo.buildingInfo.dto;

import com.bankersCalculator.server.housingInfo.buildingInfo.common.RentHousingType;
import lombok.Getter;

@Getter
public class RentTransactionInquiryRequest {
    private String districtCodeFirst5;
    private RentHousingType rentHousingType;
    private int months;
    private String dongName;
    private String jibun;

}
