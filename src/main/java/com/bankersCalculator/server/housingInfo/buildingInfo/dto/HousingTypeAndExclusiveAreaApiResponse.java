package com.bankersCalculator.server.housingInfo.buildingInfo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class HousingTypeAndExclusiveAreaApiResponse {

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

        @JsonProperty("hstpGbCd")
        private String rentHousingTypeCode;

        @JsonProperty("hstpGbCdNm")
        private String rentHousingTypeName;

        @JsonProperty("silHoHhldCnt")
        private int houseHoldCount;

        @JsonProperty("silHoHhldArea")
        private double exclusiveArea;
    }
}
