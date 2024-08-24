package com.bankersCalculator.server.housingInfo.buildingInfo.controller;

import com.bankersCalculator.server.common.api.ApiResponse;
import com.bankersCalculator.server.housingInfo.buildingInfo.api.HousingTypeAndExclusiveAreaApiClient;
import com.bankersCalculator.server.housingInfo.buildingInfo.dto.HousingTypeAndExclusiveAreaApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/building-info")
public class HousingTypeAndExclusiveAreaApiController {
    private final HousingTypeAndExclusiveAreaApiClient housingTypeAndExclusiveAreaApiClient;

    public HousingTypeAndExclusiveAreaApiController(HousingTypeAndExclusiveAreaApiClient housingTypeAndExclusiveAreaApiClient) {
        this.housingTypeAndExclusiveAreaApiClient = housingTypeAndExclusiveAreaApiClient;
    }
    @PostMapping("/housing-type-and-exclusive-area")
    public ApiResponse<HousingTypeAndExclusiveAreaApiResponse> getHousingTypeAndExclusiveArea(
            @RequestParam String districtCodeFirst5,
            @RequestParam String districtCodeLast5,
            @RequestParam String jibunMain,
            @RequestParam String jibunSub

            ) {

        HousingTypeAndExclusiveAreaApiResponse response = housingTypeAndExclusiveAreaApiClient.getApHsTpInfo(
                districtCodeFirst5,
                districtCodeLast5,
                jibunMain,
                jibunSub
        );

        return ApiResponse.ok(response);
    }
}
