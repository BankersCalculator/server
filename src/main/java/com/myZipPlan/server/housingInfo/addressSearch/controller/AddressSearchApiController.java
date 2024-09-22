package com.myZipPlan.server.housingInfo.addressSearch.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.housingInfo.addressSearch.api.AddressSearchApiClient;
import com.myZipPlan.server.housingInfo.addressSearch.dto.AddressSearchApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/addressSearch")
@RequiredArgsConstructor
public class AddressSearchApiController {
    private final AddressSearchApiClient addressSearchApiClient;

    @GetMapping
    public ApiResponse<Map<String, Object>> searchAddress(
            @RequestBody AddressSearchApiRequest request,
            @RequestParam(defaultValue = "0") int page) throws IOException {
            Map<String, Object> response = addressSearchApiClient.searchAddress(request.getKeyword(), page);
            return ApiResponse.ok(response);
    }
}
