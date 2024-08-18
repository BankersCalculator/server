package com.bankersCalculator.server.housingInfo.addressSearch.controller;
import com.bankersCalculator.server.housingInfo.addressSearch.service.AddressSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/addressSearch")
@RequiredArgsConstructor
public class AddressSearchApiController {
    private final AddressSearchService addressSearchService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> searchAddress(@RequestParam String keyword) {
        try {
            Map<String, Object> addressSearchResult = addressSearchService.searchAddress(keyword);
            Object jusoList = addressSearchResult.get("jusoList");

            Map<String, Object> response = new HashMap<>();
            response.put("jusoList", jusoList);
            response.put("errorCode", addressSearchResult.get("errorCode"));
            response.put("errorMessage", addressSearchResult.get("errorMessage"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
