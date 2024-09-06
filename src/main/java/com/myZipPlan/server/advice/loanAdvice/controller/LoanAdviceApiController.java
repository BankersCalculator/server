package com.myZipPlan.server.advice.loanAdvice.controller;

import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.request.SimpleLoanAdviceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.request.SpecificLoanAdviceRequest;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceResponse;
import com.myZipPlan.server.advice.loanAdvice.dto.response.LoanAdviceSummaryResponse;
import com.myZipPlan.server.advice.loanAdvice.service.LoanAdviceQueryService;
import com.myZipPlan.server.advice.loanAdvice.service.LoanAdviceService;
import com.myZipPlan.server.common.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

        if (recentLoanAdvices == null) {
            return ApiResponse.noContent(null);
        }

        return ApiResponse.ok(recentLoanAdvices);
    }

    @GetMapping("/{loanAdviceResultId}")
    public ApiResponse<LoanAdviceResponse> getSpecificLoanAdvice(@PathVariable Long loanAdviceResultId) {
        LoanAdviceResponse specificLoanAdvice = loanAdviceQueryService.getSpecificLoanAdvice(loanAdviceResultId);

        return ApiResponse.ok(specificLoanAdvice);
    }

    @PostMapping("/simple")
    public ApiResponse<List<LoanAdviceSummaryResponse>> getSimpleLoanConditions(@RequestBody @Valid SimpleLoanAdviceRequest request) {
        BigDecimal rentalDeposit = request.getRentalDeposit();
        List<LoanAdviceSummaryResponse> recentLoanAdvices = loanAdviceService.getSimpleLoanConditions(rentalDeposit);

        return ApiResponse.ok(recentLoanAdvices);
    }

    @PostMapping
    public ApiResponse<LoanAdviceResponse> generateLoanAdvice(@RequestBody @Valid LoanAdviceRequest request) {

        LoanAdviceResponse loanAdviceResponse = loanAdviceService.createLoanAdvice(request.toServiceRequest());

        if (!loanAdviceResponse.getHasEligibleProduct()) {
            return ApiResponse.noContent(loanAdviceResponse);
        }
        return ApiResponse.ok(loanAdviceResponse);
    }

    @PostMapping("/specific")
    public ApiResponse<LoanAdviceResponse> generateLoanAdviceOnSpecificLoan(@RequestBody SpecificLoanAdviceRequest request) {
        LoanAdviceResponse loanAdviceResponse = loanAdviceService.generateLoanAdviceOnSpecificLoan(request.getUserInputInfoId(), request.getProductCode());

        return ApiResponse.ok(loanAdviceResponse);
    }
}
