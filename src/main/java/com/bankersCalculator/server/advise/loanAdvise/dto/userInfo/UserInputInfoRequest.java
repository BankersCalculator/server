package com.bankersCalculator.server.advise.loanAdvise.dto.userInfo;

import com.bankersCalculator.server.common.enums.loanAdvise.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInputInfoRequest {

    private final UserType userType;
    private final String userId;

    public UserInputInfoServiceRequest toServiceRequest() {
        return UserInputInfoServiceRequest.builder()
            .userType(userType)
            .userId(userId)
            .build();
    }
}
