package com.myZipPlan.server.calculator.dtiCalc.controller;

import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcRequest;
import com.myZipPlan.server.calculator.dtiCalc.dto.DtiCalcResponse;
import com.myZipPlan.server.calculator.dtiCalc.service.DtiCalcService;
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
@RequestMapping("/api/v1/dtiCalc")
@RestController
public class DtiCalcApiController {
    private final DtiCalcService dtiCalcService;

    @PostMapping
    public ApiResponse<DtiCalcResponse> calculateDti(@Valid @RequestBody DtiCalcRequest request) {
        DtiCalcResponse dtiCalcResponse = dtiCalcService.dtiCalculate(request.toServiceRequest());
        return ApiResponse.ok(dtiCalcResponse);
    }


}
