package com.myZipPlan.server.housingInfo.addressSearch.controller;
import com.myZipPlan.server.housingInfo.addressSearch.api.AddressSearchApiClient;
import com.myZipPlan.server.housingInfo.addressSearch.dto.AddressSearchApiRequest;
import com.myZipPlan.server.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/addressSearch")
@RequiredArgsConstructor
public class AddressSearchApiController {
    private final AddressSearchApiClient addressSearchApiClient;

    @PostMapping
    public ApiResponse<Map<String, Object>> searchAddress(@RequestBody AddressSearchApiRequest request) throws IOException {
            Map<String, Object> response = addressSearchApiClient.searchAddress(request.getKeyword());
            return ApiResponse.ok(response);
    }
}
