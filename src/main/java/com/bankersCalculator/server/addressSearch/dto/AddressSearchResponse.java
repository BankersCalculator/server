package com.bankersCalculator.server.addressSearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressSearchResponse {

    @JacksonXmlProperty(localName = "common")
    private Common common;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "juso")
    private List<Juso> jusoList;

    // Getters and setters

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Common {
        @JacksonXmlProperty(localName = "totalCount")
        private int totalCount;

        @JacksonXmlProperty(localName = "currentPage")
        private int currentPage;

        @JacksonXmlProperty(localName = "countPerPage")
        private int countPerPage;

        @JacksonXmlProperty(localName = "errorCode")
        private int errorCode;

        @JacksonXmlProperty(localName = "errorMessage")
        private String errorMessage;

        // Getters and setters
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Juso {
        @JacksonXmlProperty(localName = "roadAddr")
        private String roadAddr;

        @JacksonXmlProperty(localName = "roadAddrPart1")
        private String roadAddrPart1;

        @JacksonXmlProperty(localName = "roadAddrPart2")
        private String roadAddrPart2;

        @JacksonXmlProperty(localName = "jibunAddr")
        private String jibunAddr;

        @JacksonXmlProperty(localName = "engAddr")
        private String engAddr;

        @JacksonXmlProperty(localName = "zipNo")
        private String zipNo;

        @JacksonXmlProperty(localName = "admCd")
        private String admCd;

        // Getters and setters
    }
}
