package com.bankersCalculator.server.advise.loanAdvise.dto;

import com.bankersCalculator.server.common.enums.loanAdvise.RentalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalCostDto {
    private RentalType rentalType;
    private long rentalDeposit;
    private long monthlyRent;
}