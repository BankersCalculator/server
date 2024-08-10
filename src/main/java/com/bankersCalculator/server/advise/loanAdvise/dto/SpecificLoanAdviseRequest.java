package com.bankersCalculator.server.advise.loanAdvise.dto;

import com.bankersCalculator.server.common.enums.loanAdvise.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpecificLoanAdviseRequest {

    private final Long userId;
    private UserType userType;
    private final Long adviseResultId;


}