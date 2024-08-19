package com.bankersCalculator.server.advise.userInputInfo.dto;

import com.bankersCalculator.server.common.enums.loanAdvise.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInputInfoRequest {

    private final String userInputInfoId;

    public UserInputInfoServiceRequest toServiceRequest() {
        return UserInputInfoServiceRequest.builder()
            .userInputInfoId(userInputInfoId)
            .build();
    }
}
