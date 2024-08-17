package com.bankersCalculator.server.addressSearch.dto;

import lombok.Getter;
import lombok.Setter;
/*
주소검색 API 주소 :  https://business.juso.go.kr/addrlink/main.do?cPath=99MM
사이트명 : 주소기반산업지원서비스
 */
@Getter
@Setter
public class AddressSearchResponse {
    private String roadAddr; //전체 도로명주소
    private String jibunAddr; //지번주소
    private String zipNo; //우편번호
    private String admCd; //행정구역코드(법정동코드)
    private String rnMgtSn; //도로명코드
    private String bdMgtSn; //건물관리코드
    private String detBdNmList; //상세건물명
    private String bdNm; //건물명
    private String bdKdcd; //공동주택여부 (1: 공동주택, 0: 비공동주택)
    private String siNm; //시도명
    private String sggNm; //시군구명
    private String emdNm; //읍면동명
    private String liNm;  //법정리명
    private String rn;    //도로명
    private String udrtYn;//지하여부 (0:지상, 1:지하)
    private int buldMnnm; //건물본번
    private int buldSlno; //건물부번
    private String mtYn;  //산여부 (0:대지, 1:산)
    private int lnbrMnnm; //지번본번(번지)
    private int lnbrSlno; //지번부번(호)
    private String emdNo; //읍면동일련번호
}
