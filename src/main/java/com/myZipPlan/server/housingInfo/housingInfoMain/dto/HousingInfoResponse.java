package com.myZipPlan.server.housingInfo.housingInfoMain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HousingInfoResponse {
        private String rentHousingTypeName;
    private Double exclusiveArea;
    private Double averageDeposit;
    private Double averageMonthlyRent;
    private Integer transactionCount;
}
