package com.myZipPlan.server.housingInfo.buildingInfo.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.housingInfo.buildingInfo.api.HousingTypeAndExclusiveAreaApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
