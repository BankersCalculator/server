package com.bankersCalculator.server.housingInfo.addressSearch.controller;

import com.bankersCalculator.server.housingInfo.addressSearch.dto.AddressSearchRequest;
import com.bankersCalculator.server.housingInfo.addressSearch.service.AddressSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/addressSearch")
@RequiredArgsConstructor
public class AddressSearchApiController {
    private final AddressSearchService addressSearchService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> searchAddress(@RequestBody AddressSearchRequest request) {
        try {
            Map<String, Object> response = addressSearchService.searchAddress(request.getKeyword());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
