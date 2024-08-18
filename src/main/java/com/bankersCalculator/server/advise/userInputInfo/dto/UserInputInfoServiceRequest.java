package com.bankersCalculator.server.advise.userInputInfo.dto;

import com.bankersCalculator.server.common.enums.loanAdvise.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInputInfoServiceRequest {

    private final String userInputInfoId;
}
