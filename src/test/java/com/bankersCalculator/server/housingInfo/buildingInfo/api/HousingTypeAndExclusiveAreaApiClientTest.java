package com.bankersCalculator.server.housingInfo.buildingInfo.api;

import com.bankersCalculator.server.housingInfo.buildingInfo.dto.HousingTypeAndExclusiveAreaApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

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
        String districtCodeFirst5 = "11680"; // 서울시 특정 시군구 코드 11680
        String districtCodeLast5 = "10100";  // 특정 법정동 코드 10100

        String jibunMain = "0603";           // 지번 본번 0603
        String jibunSub = "000";            // 지번 부번 0005

        // 실제 API 호출
        Map<String, Object> response = housingTypeAndExclusiveAreaApiClient.InquiryHousingTypeAndExclusiveArea(
                districtCodeFirst5,
                districtCodeLast5,
                jibunMain,
                jibunSub
        );

        // 응답 검증 및 로그 출력
        assertNotNull(response, "The response should not be null");
        System.out.println("Response Header: ResultCode=" + response.get("apiResultCode") +
                ", ResultMsg=" + response.get("apiResultMessage"));

        List<HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem> itemList = (List<HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem>)response.get("housingTypeAndExclusiveAreaList");

        if (response.get("apiResultCode") == "Y") {
            System.out.println("First Item Details: ");
            System.out.println("  Rent Housing Type Code: " + itemList.get(0).getRentHousingTypeCode());
            System.out.println("  Rent Housing Type Name: " + itemList.get(0).getRentHousingTypeName());
            System.out.println("  Household Count: " + itemList.get(0).getHouseHoldCount());
            System.out.println("  Exclusive Area: " + itemList.get(0).getExclusiveArea());
        }


        System.out.println("Test passed successfully. Retrieved items:");
        for (HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem item : itemList) {
            System.out.println("  - Item: Rent Housing Type Code=" + item.getRentHousingTypeCode() +
                    ", Rent Housing Type Name=" + item.getRentHousingTypeName() +
                    ", Household Count=" + item.getHouseHoldCount() +
                    ", Exclusive Area=" + item.getExclusiveArea());
        }
    }
}

