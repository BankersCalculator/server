package com.myZipPlan.server.housingInfo.rentTransactionInquiry.controller;

import com.myZipPlan.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import com.myZipPlan.server.housingInfo.rentTransactionInquiry.dto.RentTransactionInquiryResponse;
import com.myZipPlan.server.housingInfo.rentTransactionInquiry.service.RentTransactionInquiryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RentTransactionInquiryController {

    private final RentTransactionInquiryService inquiryService;

    public RentTransactionInquiryController(RentTransactionInquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping("/rent-transactions")
    public RentTransactionInquiryResponse getRentTransactions(
            @RequestParam String lawdCd,
            @RequestParam RentHousingType rentHousingType,
            @RequestParam int months,
            @RequestParam String emdNm,
            @RequestParam String jibun
    ) throws IOException {
        return inquiryService.getRentTransactionsResult(lawdCd, rentHousingType, months, emdNm, jibun);
    }
}
