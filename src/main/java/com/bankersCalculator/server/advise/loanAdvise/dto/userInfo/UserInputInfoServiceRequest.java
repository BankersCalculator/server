package com.bankersCalculator.server.advise.loanAdvise.dto.userInfo;

import com.bankersCalculator.server.common.enums.loanAdvise.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInputInfoServiceRequest {

    private final UserType userType;
    private final String userId;
}
