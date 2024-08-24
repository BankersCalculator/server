package com.bankersCalculator.server.housingInfo.buildingInfo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RentTransactionApiResponse {

    @JsonProperty("header")
    private ApiResponseHeader header;

    @JsonProperty("body")
    private ApiResponseBody body;

    @Getter
    @Setter
    public static class ApiResponseHeader {
        @JsonProperty("resultCode")
        private String resultCode;

        @JsonProperty("resultMsg")
        private String resultMsg;
    }

    @Getter
    @Setter
    public static class ApiResponseBody {
        @JsonProperty("items")
        private ApiResponseItems items;

        @JsonProperty("numOfRows")
        private int numOfRows;

        @JsonProperty("pageNo")
        private int pageNo;

        @JsonProperty("totalCount")
        private int totalCount;
    }

    @Getter
    @Setter
    public static class ApiResponseItems {
        @JsonProperty("item")
        private List<ApiResponseItem> itemList;
    }

    @Getter
    @Setter
    public static class ApiResponseItem {
        @JsonProperty("aptNm")
        private String aptNm;

        @JsonProperty("buildYear")
        private String buildYear;

        @JsonProperty("contractTerm")
        private String contractTerm;

        @JsonProperty("contractType")
        private String contractType;

        @JsonProperty("dealDay")
        private String dealDay;

        @JsonProperty("dealMonth")
        private String dealMonth;

        @JsonProperty("dealYear")
        private String dealYear;

        @JsonProperty("deposit")
        private String deposit;

        @JsonProperty("excluUseAr")
        private String excluUseAr;

        @JsonProperty("floor")
        private String floor;

        @JsonProperty("jibun")
        private String jibun;

        @JsonProperty("monthlyRent")
        private String monthlyRent;

        @JsonProperty("preDeposit")
        private String preDeposit;

        @JsonProperty("preMonthlyRent")
        private String preMonthlyRent;

        @JsonProperty("sggCd")
        private String sggCd;

        @JsonProperty("umdNm")
        private String umdNm;

        @JsonProperty("useRRRight")
        private String useRRRight;
    }
}
