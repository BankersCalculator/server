package com.myZipPlan.server.housingInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HousingInfoApiResponse {
    private String rentHousingTypeName;
    private Double exclusiveArea;
    private Double averageDeposit;
    private Double averageMonthlyRent;
    private Integer transactionCount;
}
