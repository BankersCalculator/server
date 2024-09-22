package com.myZipPlan.server.housingInfo.buildingInfo.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.housingInfo.buildingInfo.api.HousingTypeAndExclusiveAreaApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/housingTypeAndExclusiveArea")
@RequiredArgsConstructor
public class HousingTypeAndExclusiveAreaApiController {
    private final HousingTypeAndExclusiveAreaApiClient housingTypeAndExclusiveAreaApiClient;

    @GetMapping
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
