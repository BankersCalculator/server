package com.bankersCalculator.server.advise.loanAdvise.controller;

import com.bankersCalculator.server.advise.loanAdvise.dto.LoanAdviseRequest;
import com.bankersCalculator.server.advise.loanAdvise.dto.LoanAdviseResponse;
import com.bankersCalculator.server.advise.loanAdvise.dto.UserInputInfoRequest;
import com.bankersCalculator.server.advise.loanAdvise.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advise.loanAdvise.service.LoanAdviseService;
import com.bankersCalculator.server.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/loanAdvise")
@RestController
public class LoanAdviseApiController {

    private final LoanAdviseService loanAdviseService;

    @GetMapping("/userInfo")
    public ApiResponse<UserInputInfoResponse> getSubmittedUserInput(@RequestBody UserInputInfoRequest request) {
        UserInputInfoResponse submittedUserInput = loanAdviseService.getSubmittedUserInput(request.toServiceRequest());


        return ApiResponse.ok(submittedUserInput);
    }

    @PostMapping
    public ApiResponse<LoanAdviseResponse> generateLoanAdvise(@RequestBody LoanAdviseRequest request) {
        LoanAdviseResponse loanAdviseResponse = loanAdviseService.generateLoanAdvise(request.toServiceRequest());

        return ApiResponse.ok(loanAdviseResponse);
    }

    @PostMapping("/{productCode}")
    public ApiResponse<LoanAdviseResponse> generateLoanAdviseOnSpecificLoan(@PathVariable String productCode,
                                                               @RequestParam Long userId,
                                                               @RequestParam Long adviseResultId) {
        LoanAdviseResponse loanAdviseResponse = loanAdviseService.generateLoanAdviseOnSpecificLoan(productCode, userId, adviseResultId);

        return ApiResponse.ok(loanAdviseResponse);
    }
}
