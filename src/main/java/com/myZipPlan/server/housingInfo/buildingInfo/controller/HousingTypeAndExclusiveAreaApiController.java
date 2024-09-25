package com.myZipPlan.server.housingInfo.buildingInfo.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.housingInfo.buildingInfo.api.HousingTypeAndExclusiveAreaApiClient;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.HousingTypeAndExclusiveAreaApiRequest;
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
            @RequestBody HousingTypeAndExclusiveAreaApiRequest request) {
        Map<String, Object> response = housingTypeAndExclusiveAreaApiClient.InquiryHousingTypeAndExclusiveArea(
                request.getDistrictCodeFirst5(),
                request.getDistrictCodeLast5(),
                request.getJibunMain(),
                request.getJibunSub()
        );
        return ApiResponse.ok(response);
    }
}
