package com.myZipPlan.server.housingInfo.buildingInfo.controller;

import com.myZipPlan.server.common.api.ApiResponse;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.RentTransactionInquiryRequest;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.RentTransactionInquiryResponse;
import com.myZipPlan.server.housingInfo.buildingInfo.service.RentTransactionInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/rentTransactionInquiry")
@RequiredArgsConstructor
public class RentTransactionInquiryController {
    private final RentTransactionInquiryService rentTransactionInquiryService;

    @PostMapping
    public ApiResponse<RentTransactionInquiryResponse> getRentTransactions(@RequestBody RentTransactionInquiryRequest request) {
        RentTransactionInquiryResponse rentTransactionInquiryResponse = rentTransactionInquiryService.getRentTransactions(request.getDistrictCodeFirst5()
                , request.getRentHousingType()
                , request.getMonths()
                , request.getDongName()
                , request.getJibun());
        return ApiResponse.ok(rentTransactionInquiryResponse);
    }
}
