package com.bankersCalculator.server.addressSearch.controller;

import com.bankersCalculator.server.addressSearch.dto.AddressResponse;
import com.bankersCalculator.server.addressSearch.service.AddressSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {
    private final AddressSearchService addressSearchService;

    @GetMapping("/addressSearch")
    public ResponseEntity<Map<String, Object>> searchAddress(@RequestParam String keyword) {
        try {
            List<AddressResponse> addressList = addressSearchService.searchAddress(keyword);
            Map<String, Object> response = new HashMap<>();
            response.put("jusoList", addressList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
