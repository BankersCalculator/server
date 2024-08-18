package com.bankersCalculator.server.advise.userInputInfo.controller;

import com.bankersCalculator.server.advise.userInputInfo.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advise.userInputInfo.dto.UserInputSummary;
import com.bankersCalculator.server.advise.userInputInfo.service.UserInputInfoService;
import com.bankersCalculator.server.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/specific")
    public ApiResponse<UserInputInfoResponse> getSpecificUserInput(@RequestParam String UserInfoInputId) {
        UserInputInfoResponse submittedUserInput = userInputInfoService.getSpecificUserInput(UserInfoInputId);

        return ApiResponse.ok(submittedUserInput);
    }

    @GetMapping("/recent-ten")
    public ApiResponse<List<UserInputSummary>> getRecentUserInputs() {
        List<UserInputSummary> recentUserInputs = userInputInfoService.getRecentUserInputs();

        return ApiResponse.ok(recentUserInputs);
    }
}
