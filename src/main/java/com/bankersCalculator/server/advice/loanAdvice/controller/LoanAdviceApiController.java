package com.bankersCalculator.server.advice.loanAdvice.controller;

import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.request.SpecificLoanAdviceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.bankersCalculator.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.bankersCalculator.server.advice.loanAdvice.service.LoanAdviceQueryService;
import com.bankersCalculator.server.advice.loanAdvice.service.LoanAdviceService;
import com.bankersCalculator.server.common.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/loanAdvice")
@RestController
@Slf4j
public class LoanAdviceApiController {

    private final LoanAdviceService loanAdviceService;
    private final LoanAdviceQueryService loanAdviceQueryService;


    @GetMapping
    public ApiResponse<List<LoanAdviceSummaryResponse>> getRecentLoanAdvices() {
        List<LoanAdviceSummaryResponse> recentLoanAdvices = loanAdviceQueryService.getRecentLoanAdvices();

        return ApiResponse.ok(recentLoanAdvices);
    }

    @GetMapping("/specific/{loanAdviceResultId}")
    public ApiResponse<LoanAdviceResponse> getSpecificLoanAdvice(@PathVariable Long loanAdviceResultId) {
        LoanAdviceResponse specificLoanAdvice = loanAdviceQueryService.getSpecificLoanAdvice(loanAdviceResultId);

        return ApiResponse.ok(specificLoanAdvice);
    }

    @PostMapping
    public ApiResponse<LoanAdviceResponse> generateLoanAdvice(@RequestBody @Valid LoanAdviceRequest request) {

        log.info("lgw request: {}", request.getHasNewborn());
        LoanAdviceResponse loanAdviceResponse = loanAdviceService.createLoanAdvice(request.toServiceRequest());

        if (!loanAdviceResponse.getHasEligibleProduct()) {
            return ApiResponse.failToMakeAdvice(loanAdviceResponse);
        }
        return ApiResponse.ok(loanAdviceResponse);
    }

    @PostMapping("/specific")
    public ApiResponse<LoanAdviceResponse> generateLoanAdviceOnSpecificLoan(@RequestBody SpecificLoanAdviceRequest request) {
        LoanAdviceResponse loanAdviceResponse = loanAdviceService.generateLoanAdviceOnSpecificLoan(request.getLoanAdviceResultId(), request.getProductCode());

        return ApiResponse.ok(loanAdviceResponse);
    }
}
