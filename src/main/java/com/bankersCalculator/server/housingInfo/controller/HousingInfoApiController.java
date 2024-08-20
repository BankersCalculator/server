package com.bankersCalculator.server.housingInfo.controller;

import com.bankersCalculator.server.common.api.ApiResponse;
import com.bankersCalculator.server.housingInfo.buildingInfo.dto.HousingTypeAndExclusiveAreaApiResponse;
import com.bankersCalculator.server.housingInfo.dto.HousingInfoApiResponse;
import com.bankersCalculator.server.housingInfo.service.HousingInfoApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/housing-info")
public class HousingInfoApiController {

    private final HousingInfoApiService housingInfoApiService;

    public HousingInfoApiController(HousingInfoApiService housingInfoApiService) {
        this.housingInfoApiService = housingInfoApiService;
    }

    @GetMapping("/info")
    public ApiResponse<List<HousingInfoApiResponse>> getHousingInfo(
            @RequestParam String districtCode,
            @RequestParam String jibun,
            @RequestParam String dongName) {
        try {
            List<HousingInfoApiResponse> response = housingInfoApiService.getHousingInfo(districtCode, jibun, dongName);
            return ApiResponse.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("주택 정보를 가져오는 데 실패했습니다.", e);
        }
    }
}