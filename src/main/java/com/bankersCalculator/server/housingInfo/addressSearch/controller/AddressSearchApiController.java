package com.bankersCalculator.server.housingInfo.addressSearch.controller;

import com.bankersCalculator.server.common.api.ApiResponse;
import com.bankersCalculator.server.housingInfo.addressSearch.dto.AddressSearchRequest;
import com.bankersCalculator.server.housingInfo.addressSearch.service.AddressSearchService;
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
    private final AddressSearchService addressSearchService;

    @PostMapping
    public ApiResponse<Map<String, Object>> searchAddress(@RequestBody AddressSearchRequest request) throws IOException {
            Map<String, Object> response = addressSearchService.searchAddress(request.getKeyword());
            return ApiResponse.ok(response);
    }
}
