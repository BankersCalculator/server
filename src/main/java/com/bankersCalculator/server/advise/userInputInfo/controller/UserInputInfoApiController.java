package com.bankersCalculator.server.advise.userInputInfo.controller;

import com.bankersCalculator.server.advise.userInputInfo.dto.UserInputInfoRequest;
import com.bankersCalculator.server.advise.userInputInfo.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advise.loanAdvise.service.LoanAdviseService;
import com.bankersCalculator.server.advise.userInputInfo.service.UserInputInfoService;
import com.bankersCalculator.server.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/api/v1/userInputInfo")
@RestController
public class UserInputInfoApiController {

    private final UserInputInfoService userInputInfoService;

    @GetMapping
    public ApiResponse<UserInputInfoResponse> getRecentlySubmittedUserInput(@RequestBody UserInputInfoRequest request) {
        UserInputInfoResponse submittedUserInput = userInputInfoService.getRecentlySubmittedUserInput(request.toServiceRequest());

        return ApiResponse.ok(submittedUserInput);
    }
}
