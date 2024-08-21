package com.bankersCalculator.server.api.housingInfo.rentTransactionInquiry.api;

import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.api.RentTransactionApiClient;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.dto.RentTransactionApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RentTransactionApiClientTest {

    @Autowired
    private RentTransactionApiClient rentTransactionApiClient;

    @BeforeEach
    public void setUp() {
        // 테스트 전에 필요한 설정이 있으면 여기서 수행
    }

    @Test
    public void testRentTransactionCallApi() throws IOException {
        // 실제 API 호출을 위한 테스트 데이터 설정
        String lawdCd = "11110"; // 예: 서울특별시 강남구
        String dealYmd = "202407"; // 예: 2024년 7월
        RentHousingType rentHousingType = RentHousingType.APARTMENT;

        // 실제 API 호출
        RentTransactionApiResponse response = rentTransactionApiClient.RentTransactionCallApi(lawdCd, dealYmd, rentHousingType);

        // 응답 데이터 검증
        assertNotNull(response);
        assertNotNull(response.getHeader());
        assertNotNull(response.getBody());

        // resultCode 및 resultMsg 로그 출력
        String resultCode = response.getHeader().getResultCode();
        String resultMsg = response.getHeader().getResultMsg();
        System.out.println("API 응답 코드 (resultCode): " + resultCode);
        System.out.println("API 응답 메시지 (resultMsg): " + resultMsg);

        // 로그를 통해 전체 응답 출력
        System.out.println("API 응답 헤더: " + response.getHeader());
        System.out.println("API 응답 바디: " + response.getBody());

        // 각 item별로 데이터 확인
        List<RentTransactionApiResponse.ApiResponseItem> items = response.getBody().getItems().getItemList();
        assertTrue(items.size() > 0, "API에서 반환된 아이템이 없습니다.");

        for (RentTransactionApiResponse.ApiResponseItem item : items) {
            System.out.println("아파트 이름: " + item.getAptNm());
            System.out.println("건축 연도: " + item.getBuildYear());
            System.out.println("계약 기간: " + item.getContractTerm());
            System.out.println("계약 유형: " + item.getContractType());
            System.out.println("거래일: " + item.getDealDay());
            System.out.println("계약 월: " + item.getDealMonth());
            System.out.println("계약 년도: " + item.getDealYear());
            System.out.println("보증금: " + item.getDeposit());
            System.out.println("전용면적: " + item.getExcluUseAr());
            System.out.println("층수: " + item.getFloor());
            System.out.println("지번: " + item.getJibun());
            System.out.println("월세 금액: " + item.getMonthlyRent());
            System.out.println("이전 보증금: " + item.getPreDeposit());
            System.out.println("이전 월세 금액: " + item.getPreMonthlyRent());
            System.out.println("시군구 코드: " + item.getSggCd());
            System.out.println("읍면동 이름: " + item.getUmdNm());
            System.out.println("사용권리: " + item.getUseRRRight());
            System.out.println("--------------");

        }
    }
}
