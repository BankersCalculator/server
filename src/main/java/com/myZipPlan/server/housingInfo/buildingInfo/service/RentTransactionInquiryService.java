package com.myZipPlan.server.housingInfo.buildingInfo.service;

import com.myZipPlan.server.housingInfo.buildingInfo.api.RentTransactionApiClient;
import com.myZipPlan.server.housingInfo.buildingInfo.common.RentHousingType;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.RentTransactionApiResponse;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.RentTransactionInquiryResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RentTransactionInquiryService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
    private static final int BASE_MONTH_OFFSET = 1;

    private final RentTransactionApiClient apiClient;

    public RentTransactionInquiryService(RentTransactionApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * 임대 거래 정보를 조회하고 필터링하여 결과를 반환합니다.
     *
     * @param districtCodeFirst5 법정동 코드
     * @param rentHousingType    주택 유형
     * @param months             조회할 개월 수
     * @param dongName           동 이름
     * @param jibun              지번
     * @return 임대 거래 조회 결과
     */
    public RentTransactionInquiryResponse getRentTransactions(String districtCodeFirst5, RentHousingType rentHousingType, int months, String dongName, String jibun) {
        LocalDate currentDate = LocalDate.now();

        List<RentTransactionInquiryResponse.TransactionDetail> transactions = generateDateRange(currentDate, months)
                .flatMap(dealYmd -> fetchTransactionsForMonth(districtCodeFirst5, dealYmd, rentHousingType, dongName, jibun))
                .collect(Collectors.toList());

        Map<String, RentTransactionInquiryResponse.AverageInfo> averageInfoMap = calculateAverageByExcluUseAr(transactions);
        return new RentTransactionInquiryResponse(averageInfoMap, transactions);
    }

    /**
     * 요청된 개월 수에 따른 날짜 범위를 생성합니다.
     *
     * @param currentDate 현재 날짜
     * @param months      조회할 개월 수
     * @return 날짜 범위 스트림
     */
    private Stream<String> generateDateRange(LocalDate currentDate, int months) {
        return currentDate.minusMonths(BASE_MONTH_OFFSET)
                .minusMonths(months - 1)
                .datesUntil(currentDate.minusMonths(BASE_MONTH_OFFSET).plusMonths(1), java.time.Period.ofMonths(1))
                .map(date -> date.format(DATE_FORMATTER));
    }

    /**
     * 특정 월에 대한 임대 거래 데이터를 조회합니다.
     *
     * @param districtCodeFirst5 법정동 코드
     * @param dealYmd            거래 연월
     * @param rentHousingType    주택 유형
     * @param dongName           동 이름
     * @param jibun              지번
     * @return 필터링된 거래 데이터 스트림
     */
    private Stream<RentTransactionInquiryResponse.TransactionDetail> fetchTransactionsForMonth(String districtCodeFirst5, String dealYmd, RentHousingType rentHousingType, String dongName, String jibun) {
        try {
            Map<String, Object> apiResponse = apiClient.inquiryRentTransaction(districtCodeFirst5, dealYmd, rentHousingType);
            List<RentTransactionApiResponse.ApiResponseItem> items = extractTransactionItems(apiResponse);

            return items.stream()
                    .filter(item -> item.getUmdNm().equals(dongName) && item.getJibun().equals(jibun))
                    .map(this::convertToTransactionDetail);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch rent transactions for the month: " + dealYmd, e);
        }
    }

    /**
     * API 응답에서 거래 항목 목록을 추출합니다.
     *
     * @param apiResponse API 응답 데이터
     * @return 거래 항목 리스트
     */
    @SuppressWarnings("unchecked")
    private List<RentTransactionApiResponse.ApiResponseItem> extractTransactionItems(Map<String, Object> apiResponse) {
        return (List<RentTransactionApiResponse.ApiResponseItem>) apiResponse.getOrDefault("rentTransactionInfoList", List.of());
    }

    /**
     * 거래 항목을 TransactionDetail 객체로 변환합니다.
     *
     * @param item 임대 거래 항목
     * @return 변환된 TransactionDetail 객체
     */
    private RentTransactionInquiryResponse.TransactionDetail convertToTransactionDetail(RentTransactionApiResponse.ApiResponseItem item) {
        return new RentTransactionInquiryResponse.TransactionDetail(
                item.getAptNm(),
                item.getDealYear(),
                item.getDealMonth(),
                item.getDeposit(),
                item.getExcluUseAr(),
                item.getFloor(),
                item.getMonthlyRent(),
                item.getContractType()
        );
    }

    /**
     * 평형별 보증금, 월세 평균 및 건수를 계산합니다.
     *
     * @param transactions 거래 세부 정보 리스트
     * @return 평형별 평균 정보 맵
     */
    private Map<String, RentTransactionInquiryResponse.AverageInfo> calculateAverageByExcluUseAr(List<RentTransactionInquiryResponse.TransactionDetail> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        RentTransactionInquiryResponse.TransactionDetail::getExcluUseAr,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                this::calculateAverageInfo
                        )
                ));
    }

    /**
     * 특정 평형의 평균 보증금, 월세 및 거래 건수를 계산합니다.
     *
     * @param details 거래 세부 정보 리스트
     * @return 계산된 AverageInfo 객체
     */
    private RentTransactionInquiryResponse.AverageInfo calculateAverageInfo(List<RentTransactionInquiryResponse.TransactionDetail> details) {
        double avgDeposit = details.stream()
                .mapToDouble(detail -> parseDouble(detail.getDeposit()))
                .average()
                .orElse(0);

        double avgMonthlyRent = details.stream()
                .mapToDouble(detail -> parseDouble(detail.getMonthlyRent()))
                .average()
                .orElse(0);

        return new RentTransactionInquiryResponse.AverageInfo(avgDeposit, avgMonthlyRent, details.size());
    }

    /**
     * 문자열을 double로 변환합니다. 예외 발생 시 0을 반환합니다.
     *
     * @param value 변환할 문자열
     * @return 변환된 double 값
     */
    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.replace(",", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
