package com.myZipPlan.server.housingInfo.housingInfoMain.controller;
import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.housingInfo.housingInfoMain.dto.HousingInfoRequest;
import com.myZipPlan.server.housingInfo.housingInfoMain.service.HousingInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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

