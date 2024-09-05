package com.bankersCalculator.server.housingInfo.buildingInfo.controller;

import com.bankersCalculator.server.common.api.ApiResponse;
import com.bankersCalculator.server.housingInfo.buildingInfo.api.HousingTypeAndExclusiveAreaApiClient;
import com.bankersCalculator.server.housingInfo.buildingInfo.dto.HousingTypeAndExclusiveAreaApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/housingTypeAndExclusiveAreaApi")
@RequiredArgsConstructor
public class HousingTypeAndExclusiveAreaApiController {
    private final HousingTypeAndExclusiveAreaApiClient housingTypeAndExclusiveAreaApiClient;

    @PostMapping
    public ApiResponse<Map<String, Object>> getHousingTypeAndExclusiveArea(
            @RequestParam String districtCodeFirst5,
            @RequestParam String districtCodeLast5,
            @RequestParam String jibunMain,
            @RequestParam String jibunSub

            ) {
        Map<String, Object> response = housingTypeAndExclusiveAreaApiClient.InquiryHousingTypeAndExclusiveArea(
                districtCodeFirst5,
                districtCodeLast5,
                jibunMain,
                jibunSub
        );
        return ApiResponse.ok(response);
    }
}
