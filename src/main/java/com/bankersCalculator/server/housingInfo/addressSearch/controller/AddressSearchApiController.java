package com.bankersCalculator.server.housingInfo.addressSearch.controller;

import com.bankersCalculator.server.housingInfo.addressSearch.api.AddressSearchApiClient;
import com.bankersCalculator.server.housingInfo.addressSearch.dto.AddressSearchApiRequest;
import lombok.RequiredArgsConstructor;
import com.bankersCalculator.server.common.api.ApiResponse;
import org.springframework.web.bind.annotation.*;

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
