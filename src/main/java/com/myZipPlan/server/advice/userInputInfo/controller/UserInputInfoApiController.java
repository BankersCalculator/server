package com.myZipPlan.server.advice.userInputInfo.controller;

import com.myZipPlan.server.advice.userInputInfo.dto.UserInputInfoResponse;
import com.myZipPlan.server.advice.userInputInfo.dto.UserInputSummaryResponse;
import com.myZipPlan.server.advice.userInputInfo.service.UserInputInfoService;
import com.myZipPlan.server.common.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/{userInfoInputId}")
    public ApiResponse<UserInputInfoResponse> getSpecificUserInput(@PathVariable Long userInfoInputId) {
        UserInputInfoResponse submittedUserInput = userInputInfoService.getSpecificUserInput(userInfoInputId);

        return ApiResponse.ok(submittedUserInput);
    }

    @GetMapping("/recent")
    public ApiResponse<List<UserInputSummaryResponse>> getRecentUserInputs() {
        List<UserInputSummaryResponse> recentUserInputs = userInputInfoService.getRecentUserInputs();

        return ApiResponse.ok(recentUserInputs);
    }
}
