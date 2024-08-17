package com.bankersCalculator.server.rentTransactionInquiry.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RentTransactionApiResponse {

    @JacksonXmlProperty(localName = "header")
    private ApiResponseHeader header;

    @JacksonXmlProperty(localName = "body")
    private ApiResponseBody body;

    @Getter
    @Setter
    public static class ApiResponseHeader {
        @JacksonXmlProperty(localName = "resultCode")
        private String resultCode;

        @JacksonXmlProperty(localName = "resultMsg")
        private String resultMsg;
    }

    @Getter
    @Setter
    public static class ApiResponseBody {
        @JacksonXmlProperty(localName = "items")
        private ApiResponseItems items;

        @JacksonXmlProperty(localName = "numOfRows")
        private int numOfRows;

        @JacksonXmlProperty(localName = "pageNo")
        private int pageNo;

        @JacksonXmlProperty(localName = "totalCount")
        private int totalCount;
    }

    @Getter
    @Setter
    public static class ApiResponseItems {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        private List<ApiResponseItem> itemList;
    }

    @Getter
    @Setter
    public static class ApiResponseItem {
        @JacksonXmlProperty(localName = "aptNm")
        private String aptNm;

        @JacksonXmlProperty(localName = "buildYear")
        private String buildYear;

        @JacksonXmlProperty(localName = "contractTerm")
        private String contractTerm;

        @JacksonXmlProperty(localName = "contractType")
        private String contractType;

        @JacksonXmlProperty(localName = "dealDay")
        private String dealDay;

        @JacksonXmlProperty(localName = "dealMonth")
        private String dealMonth;

        @JacksonXmlProperty(localName = "dealYear")
        private String dealYear;

        @JacksonXmlProperty(localName = "deposit")
        private String deposit;

        @JacksonXmlProperty(localName = "excluUseAr")
        private String excluUseAr;

        @JacksonXmlProperty(localName = "floor")
        private String floor;

        @JacksonXmlProperty(localName = "jibun")
        private String jibun;

        @JacksonXmlProperty(localName = "monthlyRent")
        private String monthlyRent;

        @JacksonXmlProperty(localName = "preDeposit")
        private String preDeposit;

        @JacksonXmlProperty(localName = "preMonthlyRent")
        private String preMonthlyRent;

        @JacksonXmlProperty(localName = "sggCd")
        private String sggCd;

        @JacksonXmlProperty(localName = "umdNm")
        private String umdNm;

        @JacksonXmlProperty(localName = "useRRRight")
        private String useRRRight;
    }
}
