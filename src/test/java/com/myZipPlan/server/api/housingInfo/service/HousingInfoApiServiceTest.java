package com.myZipPlan.server.api.housingInfo.service;

import com.myZipPlan.server.housingInfo.buildingInfo.api.HousingTypeAndExclusiveAreaApiClient;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.HousingTypeAndExclusiveAreaApiResponse;
import com.myZipPlan.server.housingInfo.dto.HousingInfoApiResponse;
import com.myZipPlan.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import com.myZipPlan.server.housingInfo.rentTransactionInquiry.dto.RentTransactionInquiryResponse;
import com.myZipPlan.server.housingInfo.rentTransactionInquiry.service.RentTransactionInquiryService;
import com.myZipPlan.server.housingInfo.service.HousingInfoApiService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.myZipPlan.server.housingInfo.service.HousingInfoApiService.parseJibun;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class HousingInfoApiServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(HousingInfoApiServiceTest.class);

    @Autowired
    private HousingInfoApiService housingInfoApiService;
    @Autowired
    private HousingTypeAndExclusiveAreaApiClient housingTypeAndExclusiveAreaApiClient;
    @Autowired
    private RentTransactionInquiryService rentTransactionInquiryService;

    @Test
    public void testGetHousingInfo() throws IOException {
        // 테스트 파라미터 설정
        String districtCode = "1168010100"; // 예시 법정동 코드
        String jibun = "603-5";         // 예시 지번
        String dongName = "역삼동";           // 예시 읍면동 이름

        // Step 1: districtCode와 jibun을 분리
        String districtCodeFirst5 = districtCode.substring(0, 5);
        String districtCodeLast5 = districtCode.substring(5);
        String[] parsedJibun = parseJibun(jibun);

        String jibunMain = parsedJibun[0];
        String jibunSub = parsedJibun[1];

        logger.info("Step 1: Parsed Parameters");
        logger.info("District Code First 5: {}", districtCodeFirst5);
        logger.info("District Code Last 5: {}", districtCodeLast5);
        logger.info("Jibun Main: {}", jibunMain);
        logger.info("Jibun Sub: {}", jibunSub);

        // Step 2: 주택 유형 및 전용 면적 정보
        HousingTypeAndExclusiveAreaApiResponse housingTypeInfo = housingTypeAndExclusiveAreaApiClient.getApHsTpInfo(districtCodeFirst5, districtCodeLast5, jibunMain, jibunSub);

        assertNotNull(housingTypeInfo, "HousingTypeInfo should not be null");
        logger.info("Step 2: HousingTypeInfo Response");
        logger.info("Total Items: {}", housingTypeInfo.getBody().getItems().getItemList().size());

        List<HousingInfoApiResponse> result = new ArrayList<>();

        // Step 3: housingTypeInfo에서 필요한 정보를 추출하여 리스트에 추가
        for (HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem item : housingTypeInfo.getBody().getItems().getItemList()) {
            String rentHousingTypeName = item.getRentHousingTypeName();
            double exclusiveArea = item.getExclusiveArea();
            int rentHousingTypeCode = Integer.parseInt(item.getRentHousingTypeCode());

            logger.info("Step 3: Processing Item");
            logger.info("Rent Housing Type Name: {}", rentHousingTypeName);
            logger.info("Exclusive Area: {}", exclusiveArea);
            logger.info("Rent Housing Type Code: {}", rentHousingTypeCode);

            // RentHousingType 매핑
            RentHousingType rentHousingType;
            switch (rentHousingTypeCode) {
                case 1:
                    rentHousingType = RentHousingType.APARTMENT;
                    break;
                case 2:
                    rentHousingType = RentHousingType.OFFICETEL;
                    break;
                case 3:
                case 4:
                    rentHousingType = RentHousingType.HOUSEHOLD_HOUSE;
                    break;
                case 5:
                case 6:
                    rentHousingType = RentHousingType.FAMILY_HOUSE;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Rent Housing Type Code: " + rentHousingTypeCode);
            }

            logger.info("Mapped Rent Housing Type: {}", rentHousingType);

            // Step 4: 임대 거래 데이터
            RentTransactionInquiryResponse rentTransactionResponse = rentTransactionInquiryService.getRentTransactionsResult(districtCodeFirst5, rentHousingType, 3, dongName, jibun);

            assertNotNull(rentTransactionResponse, "RentTransactionResponse should not be null");
            logger.info("Step 4: RentTransactionResponse received");
            logger.info("Transaction Count: {}", rentTransactionResponse.getTransactions().size());


            // Step 5: 평균 보증금과 거래 건수를 추출하고, 정보를 활용
            for (Map.Entry<String, RentTransactionInquiryResponse.AverageInfo> entry : rentTransactionResponse.getAverageInfoByExcluUseAr().entrySet()) {
                String excluUseAr = entry.getKey();
                RentTransactionInquiryResponse.AverageInfo avgInfo = entry.getValue();

                logger.info("평형 확인 - 전용면적: {}, 평균 보증금: {}, 평균 월세: {}, 거래 건수: {}",
                        excluUseAr, avgInfo.getAverageDeposit(), avgInfo.getAverageMonthlyRent(), avgInfo.getTransactionCount());

                // 정보를 result 리스트에 추가
                result.add(new HousingInfoApiResponse(
                        rentHousingTypeName,
                        Double.parseDouble(excluUseAr),
                        avgInfo.getAverageDeposit(),
                        avgInfo.getAverageMonthlyRent(),
                        avgInfo.getTransactionCount()
                ));
            }
        }

        // 최종 결과 확인
        logger.info("Final Result: Housing Info List");
        result.forEach(info -> {
            logger.info("Rent Housing Type Name: {}", info.getRentHousingTypeName());
            logger.info("Exclusive Area: {}", info.getExclusiveArea());
            logger.info("Average Deposit: {}", info.getAverageDeposit());
            logger.info("Transaction Count: {}", info.getTransactionCount());
        });

        // 응답 검증
        assertNotNull(result, "The response should not be null");
        assertTrue(!result.isEmpty(), "The response list should not be empty");
    }
}
