    package com.bankersCalculator.server.housingInfo.buildingInfo.service;

    import com.bankersCalculator.server.housingInfo.buildingInfo.common.RentHousingType;
    import com.bankersCalculator.server.housingInfo.buildingInfo.dto.RentTransactionInquiryResponse;
    import org.junit.jupiter.api.Test;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;

    import java.io.IOException;

    import static org.junit.jupiter.api.Assertions.assertNotNull;
    import static org.junit.jupiter.api.Assertions.assertTrue;

    @SpringBootTest
    public class RentTransactionInquiryServiceTest {

        private static final Logger logger = LoggerFactory.getLogger(RentTransactionInquiryServiceTest.class);

        @Autowired
        private RentTransactionInquiryService inquiryService;

        @Test
        public void testGetRentTransactionsResult() throws IOException {
            // Given
            String districtCodeFirst5 = "11680";  // 예시 법정동 코드 11680
            RentHousingType rentHousingType = RentHousingType.APARTMENT;
            int months = 3;
            String dongName = "역삼동";
            String jibun = "372-1";

            logger.info("테스트 시작 - 파라미터: districtCodeFirst5: {}, rentHousingType: {}, months: {}, dongName: {}, jibun: {}",
                    districtCodeFirst5, rentHousingType, months, dongName, jibun);

            // When
            RentTransactionInquiryResponse response = inquiryService.getRentTransactionsResult(districtCodeFirst5, rentHousingType, months, dongName, jibun);


            // 평형별 평균 정보에 대한 검증
            response.getAverageInfoByExcluUseAr().forEach((excluUseAr, avgInfo) -> {
                logger.info("평형 확인 - 전용면적: {}, 평균 보증금: {}, 평균 월세: {}, 거래 건수: {}",
                        excluUseAr, avgInfo.getAverageDeposit(), avgInfo.getAverageMonthlyRent(), avgInfo.getTransactionCount());

            });

            // 개별 거래 정보에 대한 검증
            response.getTransactions().forEach(transaction -> {
                logger.info("거래 확인 - 아파트명: {}, 계약 년도: {}, 계약 월: {}, 보증금: {}, 전용면적: {}, 층수: {}, 월세: {}, 계약 유형: {}",
                        transaction.getAptNm(), transaction.getDealYear(), transaction.getDealMonth(),
                        transaction.getDeposit(), transaction.getExcluUseAr(), transaction.getFloor(),
                        transaction.getMonthlyRent(), transaction.getContractType());


            });

            logger.info("테스트가 성공적으로 완료되었습니다.");
        }
    }
