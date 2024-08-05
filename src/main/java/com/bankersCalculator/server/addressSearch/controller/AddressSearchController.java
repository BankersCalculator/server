package com.bankersCalculator.server.addressSearch.controller;

import com.bankersCalculator.server.addressSearch.dto.AddressSearchResponse;
import com.bankersCalculator.server.addressSearch.service.AddressSearchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addressSearch")
@CrossOrigin(origins = "*")
public class AddressSearchController {

    private final AddressSearchService addressSearchService;

    public AddressSearchController(AddressSearchService addressSearchService) {
        this.addressSearchService = addressSearchService;
    }

    @GetMapping
    public AddressSearchResponse searchAddress(@RequestParam("keyword") String keyword) {
        return addressSearchService.searchAddress(keyword);
    }
}
