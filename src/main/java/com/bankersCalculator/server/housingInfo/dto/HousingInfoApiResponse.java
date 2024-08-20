package com.bankersCalculator.server.housingInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HousingInfoApiResponse {
    private String rentHousingTypeName;
    private double exclusiveArea;
    private double averageDeposit;
    private int transactionCount;
}
