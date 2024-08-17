package com.bankersCalculator.server.rentTransactionInquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class RentTransactionInquiryResponse {
    private Map<String, AverageInfo> averageInfoByExcluUseAr;
    private List<TransactionDetail> transactions;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TransactionDetail {
        private String aptNm;
        private String dealYear;
        private String dealMonth;
        private String deposit;
        private String excluUseAr;
        private String floor;
        private String monthlyRent;
        private String contractType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AverageInfo {
        private double averageDeposit;
        private double averageMonthlyRent;
        private int transactionCount;
    }
}
