package com.myZipPlan.server.housingInfo.housingInfoMain.controller;
import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.housingInfo.housingInfoMain.dto.HousingInfoRequest;
import com.myZipPlan.server.housingInfo.housingInfoMain.service.HousingInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/housingInfo")
@RequiredArgsConstructor
public class HousingInfoApiController {
    private final HousingInfoService housingInfoService;

    @GetMapping
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

