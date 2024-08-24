package com.bankersCalculator.server.housingInfo.buildingInfo.dto;

import com.bankersCalculator.server.housingInfo.buildingInfo.common.RentHousingType;
import lombok.Getter;

@Getter
public class RentTransactionApiRequest {
    private String districtCodeFirst5;
    private String dealYmd;
    private RentHousingType rentHousingType;
}


