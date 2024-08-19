package com.bankersCalculator.server.advice.userInputInfo.controller;

import com.bankersCalculator.server.advice.userInputInfo.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advice.userInputInfo.dto.UserInputSummaryResponse;
import com.bankersCalculator.server.advice.userInputInfo.service.UserInputInfoService;
import com.bankersCalculator.server.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api/v1/userInputInfo")
@RestController
public class UserInputInfoApiController {

    private final UserInputInfoService userInputInfoService;

    @GetMapping
    public ApiResponse<UserInputInfoResponse> getRecentlySubmittedUserInput() {
        UserInputInfoResponse submittedUserInput = userInputInfoService.getRecentlySubmittedUserInput();

        return ApiResponse.ok(submittedUserInput);
    }

    @GetMapping("/specific/{userInfoInputId}")
    public ApiResponse<UserInputInfoResponse> getSpecificUserInput(@PathVariable Long userInfoInputId) {
        UserInputInfoResponse submittedUserInput = userInputInfoService.getSpecificUserInput(userInfoInputId);

        return ApiResponse.ok(submittedUserInput);
    }

    @GetMapping("/recent-ten")
    public ApiResponse<List<UserInputSummaryResponse>> getRecentUserInputs() {
        List<UserInputSummaryResponse> recentUserInputs = userInputInfoService.getRecentUserInputs();

        return ApiResponse.ok(recentUserInputs);
    }
}
