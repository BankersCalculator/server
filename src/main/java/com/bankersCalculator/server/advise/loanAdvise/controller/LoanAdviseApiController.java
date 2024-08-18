package com.bankersCalculator.server.advise.loanAdvise.controller;

import com.bankersCalculator.server.advise.loanAdvise.dto.*;
import com.bankersCalculator.server.advise.loanAdvise.dto.userInfo.UserInputInfoRequest;
import com.bankersCalculator.server.advise.loanAdvise.dto.userInfo.UserInputInfoResponse;
import com.bankersCalculator.server.advise.loanAdvise.service.LoanAdviseService;
import com.bankersCalculator.server.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/loanAdvise")
@RestController
public class LoanAdviseApiController {

    private final LoanAdviseService loanAdviseService;

    @PostMapping
    public ApiResponse<LoanAdviseResponse> generateLoanAdvise(@RequestBody LoanAdviseRequest request) {
        LoanAdviseResponse loanAdviseResponse = loanAdviseService.generateLoanAdvise(request.toServiceRequest());

        return ApiResponse.ok(loanAdviseResponse);
    }

    @PostMapping("/{productCode}")
    public ApiResponse<LoanAdviseResponse> generateLoanAdviseOnSpecificLoan(@PathVariable String productCode,
                                                                            @RequestBody SpecificLoanAdviseRequest request) {
        LoanAdviseResponse loanAdviseResponse = loanAdviseService.generateLoanAdviseOnSpecificLoan(productCode, request.getUserId(), request.getAdviseResultId());

        return ApiResponse.ok(loanAdviseResponse);
    }
}
