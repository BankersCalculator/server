package com.bankersCalculator.server.addressSearch.dto;

import lombok.Getter;
import lombok.Setter;
/*
주소검색 API 주소 :  https://business.juso.go.kr/addrlink/main.do?cPath=99MM
사이트명 : 주소기반산업지원서비스
 */
@Getter
@Setter
public class AddressResponse {
    private String roadAddr;
    private String jibunAddr;
    private String zipNo;
    private String admCd;
    private String rnMgtSn;
    private String bdMgtSn;
    private String detBdNmList;
    private String bdNm;
    private String bdKdcd;
    private String siNm;
    private String sggNm;
    private String emdNm;
    private String liNm;
    private String rn;
    private String udrtYn;
    private int buldMnnm;
    private int buldSlno;
    private String mtYn;
    private int lnbrMnnm;
    private int lnbrSlno;
    private String emdNo;
}
