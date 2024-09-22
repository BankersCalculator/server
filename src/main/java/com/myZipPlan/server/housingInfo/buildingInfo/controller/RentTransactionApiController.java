package com.myZipPlan.server.housingInfo.buildingInfo.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.housingInfo.buildingInfo.api.RentTransactionApiClient;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.RentTransactionApiRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rentTransaction")
@RequiredArgsConstructor
public class RentTransactionApiController {
    private final RentTransactionApiClient rentTransactionApiClient;

    @GetMapping
    public ApiResponse<Map<String, Object>> inquiryRentTransaction(@RequestBody RentTransactionApiRequest request) throws IOException, JSONException {
        Map<String, Object> response = rentTransactionApiClient.inquiryRentTransaction(request.getDistrictCodeFirst5()
                                                                        , request.getDealYmd()
                                                                        , request.getRentHousingType()
                                                                        );
       return ApiResponse.ok(response);
    }
}
