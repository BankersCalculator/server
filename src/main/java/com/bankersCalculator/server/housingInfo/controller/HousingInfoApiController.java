package com.bankersCalculator.server.housingInfo.controller;

import com.bankersCalculator.server.common.api.ApiResponse;
import com.bankersCalculator.server.housingInfo.dto.HousingInfoResponse;
import com.bankersCalculator.server.housingInfo.dto.HousingInfoRequest;
import com.bankersCalculator.server.housingInfo.service.HousingInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/housing-info")
public class HousingInfoApiController {

    private final HousingInfoService housingInfoService;

    public HousingInfoApiController(HousingInfoService housingInfoService) {
        this.housingInfoService = housingInfoService;
    }

    @PostMapping("/info")
    public ApiResponse<List<HousingInfoResponse>> getHousingInfo(
            @RequestBody HousingInfoRequest housingInfoRequest) {
        try {
            List<HousingInfoResponse> response = housingInfoService.getHousingInfo(
                    housingInfoRequest.getDistrictCode(),
                    housingInfoRequest.getJibun(),
                    housingInfoRequest.getDongName()
            );
            return ApiResponse.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("주택 정보를 가져오는 데 실패했습니다.", e);
        }
    }
}

