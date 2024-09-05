package com.bankersCalculator.server.housingInfo.controller;

import com.bankersCalculator.server.common.api.ApiResponse;
import com.bankersCalculator.server.housingInfo.dto.HousingInfoResponse;
import com.bankersCalculator.server.housingInfo.dto.HousingInfoRequest;
import com.bankersCalculator.server.housingInfo.service.HousingInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/housingInfoApi")
@RequiredArgsConstructor
public class HousingInfoApiController {
    private final HousingInfoService housingInfoService;

    @PostMapping
    public ApiResponse<Map<String, Object>> getHousingInfo(
            @RequestBody HousingInfoRequest housingInfoRequest)  throws IOException {

            Map<String, Object> response = housingInfoService.getHousingInfo(
                    housingInfoRequest.getDistrictCode(),
                    housingInfoRequest.getJibun(),
                    housingInfoRequest.getDongName()
            );
            return ApiResponse.ok(response);
    }
}

