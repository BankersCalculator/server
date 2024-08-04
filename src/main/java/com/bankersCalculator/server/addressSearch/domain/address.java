package com.bankersCalculator.server.addressSearch.domain;

import jakarta.persistence.*;
/*
(유저 시나리오1)
사용자 키워드 입력 > 주소검색API호출 및 반환 > 사용자주소 선택 > {"법정동코드", "평형", "아파트명"} 전달
> "법정동코드"로 실거래가 조회 > 실거래가 API 3회 호출 (직전월기준 3개월 내, 5~7월)  > "평형", "아파트명" 부합 전세보증금 최고/최저/평균값/중위값/총건수, 월세 최고/최저/평균/중위값/총건수 반환

(API방식)
주소검색 API (도로명주소/상세주소 각각 존재) >  실거래가 API로 가야함.

에러코드	에러메세지	설명
1	APPLICATION ERROR	어플리케이션 에러
4	HTTP_ERROR	HTTP 에러
12	NO_OPENAPI_SERVICE_ERROR	해당 오픈 API 서비스가 없거나 폐기됨
20	SERVICE_ACCESS_DENIED_ERROR	서비스 접근거부
22	LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR	서비스 요청제한횟수 초과에러
30	SERVICE_KEY_IS_NOT_REGISTERED_ERROR	등록되지 않은 서비스키
31	DEADLINE_HAS_EXPIRED_ERROR	활용기간 만료
32	UNREGISTERED_IP_ERROR	등록되지 않은 IP
99	UNKNOWN_ERROR	기타에러

국토교통부 전월세 실거래가 API
@param LAWD_CD, 지역코드, number,  각 지역별 코드 행정표준코드관리시스템(www.code.go.kr)의 법정동코드 10자리 중 앞 5자리
       DEAL_YMD, 계약월, string, 실거래 자료의 계약년월(6자리)
       serviceKey : EOKGlRDintEZcHeH5tnMFZhkvd1WGjIPchUi1RHeI3pxw5e2y196Cijpr2zTwTZ4QnDJ7%2BpW3J2FnyVXkFfXTA%3D%3D
       uri : https://apis.data.go.kr/1613000/RTMSDataSvcAptRent/getRTMSDataSvcAptRent
@return
{
  "header": {
    "resultCode": "string",
    "resultMsg": "string"
  },
  "body": {
    "items": {
      "item": {
        "sggCd": "string", 지역코드
        "umdNm": "string", 법정동
        "aptNm": "string", 단지명
        "jibun": "string", 지번
        "excluUseAr": "string", 전용면적
        "dealYear": "string", 계약년도
        "dealMonth": "string", 계약월
        "dealDay": "string", 계약일
        "deposit": "string", 보증금
        "monthlyRent": "string", 월세
        "floor": "string", 층
        "buildYear": "string", 건축년
        "contractTerm": "string", 계약기간
        "contractType": "string", 계약구분
        "useRRRight": "string", 갱신요구권사용
        "preDeposit": "string", 종전계약보증금
        "preMonthlyRent": "string", 종전계약월세
      }
    },
    "totalCount": 0, 전체결과수
    "numOfRows": 0, 한페이지 결과수
    "pageNo": 0, 페이지 번호
  }
}

 */
@Entity
public class address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)

}
