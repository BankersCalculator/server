    package com.bankersCalculator.server.api.housingInfo.rentTransactionInquiry.service;

    import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
    import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.dto.RentTransactionInquiryResponse;
    import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.service.RentTransactionInquiryService;
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
            String lawdCd = "11680";  // 예시 법정동 코드
            RentHousingType rentHousingType = RentHousingType.APARTMENT;
            int months = 3;
            String emdNm = "삼성동";
            String jibun = "189";

            logger.info("테스트 시작 - 파라미터: lawdCd: {}, rentHousingType: {}, months: {}, emdNm: {}, jibun: {}",
                    lawdCd, rentHousingType, months, emdNm, jibun);

            // When
            RentTransactionInquiryResponse response = inquiryService.getRentTransactionsResult(lawdCd, rentHousingType, months, emdNm, jibun);

            // Then
            assertNotNull(response, "응답은 null이 아니어야 합니다.");
            assertNotNull(response.getAverageInfoByExcluUseAr(), "평형별 평균 정보는 null이 아니어야 합니다.");
            assertNotNull(response.getTransactions(), "거래 목록은 null이 아니어야 합니다.");
            assertTrue(!response.getTransactions().isEmpty(), "거래 목록은 비어있지 않아야 합니다.");

            // 평형별 평균 정보에 대한 검증
            response.getAverageInfoByExcluUseAr().forEach((excluUseAr, avgInfo) -> {
                logger.info("평형 확인 - 전용면적: {}, 평균 보증금: {}, 평균 월세: {}, 거래 건수: {}",
                        excluUseAr, avgInfo.getAverageDeposit(), avgInfo.getAverageMonthlyRent(), avgInfo.getTransactionCount());

    //            assertTrue(avgInfo.getAverageDeposit() >= 0, "평균 보증금은 0 이상이어야 합니다.");
    //            assertTrue(avgInfo.getAverageMonthlyRent() >= 0, "평균 월세는 0 이상이어야 합니다.");
    //            assertTrue(avgInfo.getTransactionCount() > 0, "거래 건수는 0보다 커야 합니다.");
            });

            // 개별 거래 정보에 대한 검증
            response.getTransactions().forEach(transaction -> {
                logger.info("거래 확인 - 아파트명: {}, 계약 년도: {}, 계약 월: {}, 보증금: {}, 전용면적: {}, 층수: {}, 월세: {}, 계약 유형: {}",
                        transaction.getAptNm(), transaction.getDealYear(), transaction.getDealMonth(),
                        transaction.getDeposit(), transaction.getExcluUseAr(), transaction.getFloor(),
                        transaction.getMonthlyRent(), transaction.getContractType());

    //            assertNotNull(transaction.getAptNm(), "아파트 이름은 null이 아니어야 합니다.");
    //            assertNotNull(transaction.getDealYear(), "계약 년도는 null이 아니어야 합니다.");
    //            assertNotNull(transaction.getDealMonth(), "계약 월은 null이 아니어야 합니다.");
    //            assertNotNull(transaction.getDeposit(), "보증금은 null이 아니어야 합니다.");
    //            assertNotNull(transaction.getExcluUseAr(), "전용면적은 null이 아니어야 합니다.");
    //            assertNotNull(transaction.getFloor(), "층수는 null이 아니어야 합니다.");
    //            assertNotNull(transaction.getMonthlyRent(), "월세는 null이 아니어야 합니다.");
    //            assertNotNull(transaction.getContractType(), "계약 유형은 null이 아니어야 합니다.");
            });

            logger.info("테스트가 성공적으로 완료되었습니다.");
        }
    }
