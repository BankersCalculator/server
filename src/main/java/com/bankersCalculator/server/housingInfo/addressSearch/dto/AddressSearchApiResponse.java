package com.bankersCalculator.server.housingInfo.addressSearch.dto;

import lombok.Getter;
import lombok.Setter;
/*
주소검색 API 주소 :  https://business.juso.go.kr/addrlink/main.do?cPath=99MM
사이트명 : 주소기반산업지원서비스
 */
@Getter
@Setter
public class AddressSearchApiResponse {
    private String roadAddress; //전체 도로명주소
    private String jibunAddress; //지번주소
    private String buildingName; //건물명

    private String districtCode; //행정구역코드(법정동코드)
    private String dongName; //읍면동명
    private String jibun; //지번(본번)-지번(부번), ex) 327-1
}
