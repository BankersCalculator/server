package com.bankersCalculator.server.advice.userInputInfo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInputInfoRequest {

    private String userInputInfoId;

    public UserInputInfoServiceRequest toServiceRequest() {
        return UserInputInfoServiceRequest.builder()
            .userInputInfoId(userInputInfoId)
            .build();
    }
}
