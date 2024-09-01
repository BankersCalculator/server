package com.myZipPlan.server.calculator.dsrCalc.cotroller;

import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcRequest;
import com.myZipPlan.server.calculator.dsrCalc.dto.DsrCalcResponse;
import com.myZipPlan.server.calculator.dsrCalc.service.DsrCalcService;
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
@RequestMapping("/api/v1/dsrCalc")
@RestController
public class DsrCalcApiController {

    private final DsrCalcService dsrCalcService;

    @PostMapping
    public ApiResponse<DsrCalcResponse> calculateDsr(@Valid @RequestBody DsrCalcRequest request) {
        DsrCalcResponse dsrCalcResponse = dsrCalcService.dsrCalculate(request.toServiceRequest());

        return ApiResponse.ok(dsrCalcResponse);
    }
}


