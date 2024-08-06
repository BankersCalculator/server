package com.bankersCalculator.server.advise.loanAdvise.dto;

import com.bankersCalculator.server.common.enums.loanAdvise.AreaSize;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.UserType;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class SpecificLoanAdviseRequest {

    private final Long userId;
    private UserType userType;
    private final Long adviseResultId;


}