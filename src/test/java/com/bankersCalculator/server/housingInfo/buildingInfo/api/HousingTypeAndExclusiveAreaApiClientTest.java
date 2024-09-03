package com.bankersCalculator.server.housingInfo.buildingInfo.api;

import com.bankersCalculator.server.housingInfo.buildingInfo.dto.HousingTypeAndExclusiveAreaApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class HousingTypeAndExclusiveAreaApiClientTest {

    @Autowired
    private HousingTypeAndExclusiveAreaApiClient housingTypeAndExclusiveAreaApiClient;

    @Test
    public void testGetApHsTpInfo() {
        // 테스트할 파라미터 설정
        String districtCodeFirst5 = "11680"; // 서울시 특정 시군구 코드
        String districtCodeLast5 = "10100";  // 특정 법정동 코드

        String jibunMain = "0603";           // 지번 본번
        String jibunSub = "0005";            // 지번 부번

        // 실제 API 호출
        HousingTypeAndExclusiveAreaApiResponse response = housingTypeAndExclusiveAreaApiClient.getApHsTpInfo(
                districtCodeFirst5,
                districtCodeLast5,
                jibunMain,
                jibunSub
        );

        // 응답 검증 및 로그 출력
        assertNotNull(response, "The response should not be null");
        System.out.println("Response Header: ResultCode=" + response.getHeader().getResultCode() +
                ", ResultMsg=" + response.getHeader().getResultMsg());

        // 응답 헤더 검증 및 로그 출력
        assertNotNull(response.getHeader(), "The response header should not be null");
        //assertEquals("00", response.getHeader().getResultCode(), "Result code should be '00'");
        //assertEquals("NORMAL SERVICE.", response.getHeader().getResultMsg(), "Result message should be 'NORMAL SERVICE.'");

        // 응답 본문 검증 및 로그 출력
        //assertNotNull(response.getBody(), "The response body should not be null");
        System.out.println("Response Body: NumOfRows=" + response.getBody().getNumOfRows() +
                ", PageNo=" + response.getBody().getPageNo() +
                ", TotalCount=" + response.getBody().getTotalCount());

        //assertNotNull(response.getBody().getItems(), "The items should not be null");
        //assertTrue(!response.getBody().getItems().getItemList().isEmpty(), "The items list should not be empty");

        //System.out.println("Items List Size: " + response.getBody().getItems().getItemList().size());

        // 첫 번째 아이템에 대한 검증 및 로그 출력
        //HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem firstItem = response.getBody().getItems().getItemList().get(0);
        //assertNotNull(firstItem.getRentHousingTypeCode(), "Rent Housing Type Code should not be null");
        //assertNotNull(firstItem.getRentHousingTypeName(), "Rent Housing Type Name should not be null");
        //assertNotNull(firstItem.getHouseHoldCount(), "Household Count should not be null");
        //assertNotNull(firstItem.getExclusiveArea(), "Exclusive Area should not be null");

        //System.out.println("First Item Details: ");
        //System.out.println("  Rent Housing Type Code: " + firstItem.getRentHousingTypeCode());
        //System.out.println("  Rent Housing Type Name: " + firstItem.getRentHousingTypeName());
        //System.out.println("  Household Count: " + firstItem.getHouseHoldCount());
        //System.out.println("  Exclusive Area: " + firstItem.getExclusiveArea());

        System.out.println("Test passed successfully. Retrieved items:");
        for (HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem item : response.getBody().getItems().getItemList()) {
            System.out.println("  - Item: Rent Housing Type Code=" + item.getRentHousingTypeCode() +
                    ", Rent Housing Type Name=" + item.getRentHousingTypeName() +
                    ", Household Count=" + item.getHouseHoldCount() +
                    ", Exclusive Area=" + item.getExclusiveArea());
        }
    }
}

