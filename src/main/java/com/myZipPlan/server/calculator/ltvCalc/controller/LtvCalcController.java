package com.myZipPlan.server.calculator.ltvCalc.controller;

import com.myZipPlan.server.calculator.ltvCalc.dto.LtvCalcRequest;
import com.myZipPlan.server.calculator.ltvCalc.dto.LtvCalcResponse;
import com.myZipPlan.server.calculator.ltvCalc.service.LtvCalcService;
import com.myZipPlan.server.common.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/ltvCalc")
@RestController
public class LtvCalcController {

    private final LtvCalcService ltvCalcService;

    @PostMapping
    public ApiResponse<LtvCalcResponse> calculateLtv(@Valid @RequestBody LtvCalcRequest ltvCalcRequest) {
        LtvCalcResponse ltvCalcResponse = ltvCalcService.calculate(ltvCalcRequest.toServiceRequest());

        return ApiResponse.ok(ltvCalcResponse);
    }
}
