package com.myZipPlan.server.housingInfo.housingInfoMain.service;

import com.myZipPlan.server.housingInfo.buildingInfo.api.HousingTypeAndExclusiveAreaApiClient;
import com.myZipPlan.server.housingInfo.buildingInfo.api.RentTransactionApiClient;
import com.myZipPlan.server.housingInfo.buildingInfo.common.RentHousingType;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.HousingTypeAndExclusiveAreaApiResponse;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.RentTransactionApiResponse;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.RentTransactionInquiryResponse;
import com.myZipPlan.server.housingInfo.housingInfoMain.dto.HousingInfoResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HousingInfoService {

    private static final int DEFAULT_MONTHS = 3;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
    private static final int BASE_MONTH_OFFSET = 1;

    private final HousingTypeAndExclusiveAreaApiClient housingTypeAndExclusiveAreaApiClient;
    private final RentTransactionApiClient rentTransactionApiClient;

    public HousingInfoService(HousingTypeAndExclusiveAreaApiClient housingTypeApiClient,
                              RentTransactionApiClient rentTransactionApiClient) {
        this.housingTypeAndExclusiveAreaApiClient = housingTypeApiClient;
        this.rentTransactionApiClient = rentTransactionApiClient;
    }

    /**
     * 특정 지역과 지번에 대한 주택 정보를 조회하고 임대 거래 정보를 그룹핑하여 반환합니다.
     *
     * @param districtCode 법정동 코드
     * @param jibun        지번
     * @param dongName     동 이름
     * @return 주택 정보와 임대 거래 정보를 포함한 Map 객체
     * @throws IOException API 호출 시 발생한 예외
     */
    public Map<String, Object> getHousingInfo(String districtCode, String jibun, String dongName) throws IOException {
        String districtCodeFirst5 = districtCode.substring(0, 5);
        String districtCodeLast5 = districtCode.substring(5);
        String[] parsedJibun = parseJibun(jibun);

        Map<String, Object> housingInfoResult = new HashMap<>();

        // Step 1: 주택 유형 및 평형 정보 조회
        Map<String, Object> housingTypeResponse = fetchHousingTypeInfo(
                districtCodeFirst5, districtCodeLast5, parsedJibun[0], parsedJibun[1]);

        housingInfoResult.put("apiResultCode", housingTypeResponse.get("apiResultCode"));
        housingInfoResult.put("apiResultMessage", housingTypeResponse.get("apiResultMessage"));

        if ("Y".equals(housingTypeResponse.get("apiResultCode"))) {
            // 참고로 사용할 주택 유형 정보 수집
            List<HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem> housingItems =
                    (List<HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem>)
                            housingTypeResponse.get("housingTypeAndExclusiveAreaList");

            // Step 2: 임대 거래 데이터 수집 및 그룹핑
            List<Map.Entry<RentTransactionInquiryResponse.TransactionDetail, String>> allTransactions =
                    collectRentTransactionData(districtCodeFirst5, housingItems, dongName, jibun);

            // Step 3: exclusiveArea와 rentHousingTypeName 기준으로 그룹핑하여 최종 HousingInfoResponse 생성
            List<HousingInfoResponse> housingInfoList = groupByExcluUseArAndCreateResponse(allTransactions);

            housingInfoResult.put("housingInfoList", housingInfoList);
        }
        return housingInfoResult;
    }

    /**
     * 임대 거래 데이터를 수집하고 각 거래와 주택 유형명을 쌍으로 반환합니다.
     *
     * @param districtCodeFirst5 법정동 코드의 앞 5자리
     * @param housingItems       주택 유형 및 전용 면적 정보 목록
     * @param dongName           동 이름
     * @param jibun              지번
     * @return 각 거래와 주택 유형명을 포함한 리스트
     * @throws IOException API 호출 시 발생한 예외
     */
    private List<Map.Entry<RentTransactionInquiryResponse.TransactionDetail, String>> collectRentTransactionData(
            String districtCodeFirst5,
            List<HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem> housingItems,
            String dongName, String jibun) throws IOException {

        LocalDate currentDate = LocalDate.now();
        List<Map.Entry<RentTransactionInquiryResponse.TransactionDetail, String>> allTransactions = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        // Step 2.1: 주택 유형 정보를 통해 임대 거래 데이터 수집
        for (String dealYmd : generateDateRange(currentDate, DEFAULT_MONTHS).collect(Collectors.toList())) {
            for (HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem item : housingItems) {
                RentHousingType rentHousingType = mapRentHousingType(item.getRentHousingTypeCode());
                String rentHousingTypeName = rentHousingType.getDescription(); // 주택 유형명 얻기

                String queryKey = districtCodeFirst5 + "-" + dealYmd + "-" + rentHousingType + "-" + dongName + "-" + jibun;
                if (!visited.contains(queryKey)) {
                    visited.add(queryKey);
                    allTransactions.addAll(fetchTransactionsForMonth(
                            districtCodeFirst5, dealYmd, rentHousingType, dongName, jibun)
                            .map(transactionDetail -> new AbstractMap.SimpleEntry<>(transactionDetail, rentHousingTypeName))
                            .collect(Collectors.toList()));
                }
            }
        }
        return allTransactions;
    }

    /**
     * 임대 거래 데이터를 전용 면적과 주택 유형명으로 그룹핑하고 HousingInfoResponse 객체로 변환합니다.
     *
     * @param transactionDetailsWithType 거래 정보와 주택 유형명 쌍의 리스트
     * @return HousingInfoResponse 객체의 리스트
     */
    private List<HousingInfoResponse> groupByExcluUseArAndCreateResponse(
            List<Map.Entry<RentTransactionInquiryResponse.TransactionDetail, String>> transactionDetailsWithType) {

        return transactionDetailsWithType.stream()
                .collect(Collectors.groupingBy(entry -> new AbstractMap.SimpleEntry<>(
                        formatExclusiveArea(Double.parseDouble(entry.getKey().getExcluUseAr())),
                        entry.getValue())))
                .entrySet().stream()
                .map(entry -> {
                    Map.Entry<String, String> key = entry.getKey();
                    List<Map.Entry<RentTransactionInquiryResponse.TransactionDetail, String>> transactionListWithType = entry.getValue();

                    String formattedExclusiveArea = key.getKey();
                    String rentHousingTypeName = key.getValue();

                    List<RentTransactionInquiryResponse.TransactionDetail> transactionList = transactionListWithType.stream()
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());

                    return createHousingInfoResponseFromTransactions(
                            formattedExclusiveArea, transactionList, rentHousingTypeName);
                })
                .collect(Collectors.toList());
    }

    /**
     * 각 전용 면적 그룹의 임대 거래 데이터로 HousingInfoResponse 객체를 생성합니다.
     *
     * @param formattedExclusiveArea 전용 면적 (포맷된 문자열)
     * @param transactionList        해당 전용 면적의 거래 목록
     * @param rentHousingTypeName    주택 유형명
     * @return HousingInfoResponse 객체
     */
    private HousingInfoResponse createHousingInfoResponseFromTransactions(String formattedExclusiveArea,
                                                                          List<RentTransactionInquiryResponse.TransactionDetail> transactionList,
                                                                          String rentHousingTypeName) {
        double avgDeposit = transactionList.stream()
                .mapToDouble(detail -> Double.parseDouble(detail.getDeposit().replace(",", "")))
                .average()
                .orElse(0);

        double avgMonthlyRent = transactionList.stream()
                .mapToDouble(detail -> Double.parseDouble(detail.getMonthlyRent().replace(",", "")))
                .average()
                .orElse(0);

        // 소수점 첫 번째 자리까지 반올림
        avgMonthlyRent = Math.round(avgMonthlyRent * 10) / 10.0;

        // exclusiveAreaPy 계산
        double exclusiveArea = Double.parseDouble(formattedExclusiveArea);
        int exclusiveAreaPy = (int) Math.round(exclusiveArea / 3.3);

        return new HousingInfoResponse(
                rentHousingTypeName,  // 주택 유형명
                exclusiveArea,        // 전용 면적
                exclusiveAreaPy,      // 평수
                avgDeposit,           // 평균 보증금
                avgMonthlyRent,       // 평균 월세
                transactionList.size() // 거래 건수
        );
    }

    /**
     * 주택 유형 및 전용 면적 정보를 조회합니다.
     *
     * @param districtCodeFirst5 법정동 코드의 앞 5자리
     * @param districtCodeLast5  법정동 코드의 뒤 5자리
     * @param jibunMain          지번의 메인 번호
     * @param jibunSub           지번의 서브 번호
     * @return 주택 유형 및 전용 면적 정보를 포함한 Map 객체
     * @throws IOException API 호출 시 발생한 예외
     */
    private Map<String, Object> fetchHousingTypeInfo(String districtCodeFirst5, String districtCodeLast5,
                                                     String jibunMain, String jibunSub) throws IOException {
        return housingTypeAndExclusiveAreaApiClient.InquiryHousingTypeAndExclusiveArea(
                districtCodeFirst5, districtCodeLast5, jibunMain, jibunSub);
    }

    /**
     * 특정 월에 대한 임대 거래 데이터를 조회합니다.
     *
     * @param districtCodeFirst5 법정동 코드의 앞 5자리
     * @param dealYmd            조회할 연월 (yyyyMM 형식)
     * @param rentHousingType    임대 주택 유형
     * @param dongName           동 이름
     * @param jibun              지번
     * @return 거래 상세 정보의 스트림
     */
    private Stream<RentTransactionInquiryResponse.TransactionDetail> fetchTransactionsForMonth(
            String districtCodeFirst5, String dealYmd,
            RentHousingType rentHousingType, String dongName, String jibun) {
        try {
            Map<String, Object> apiResponse = rentTransactionApiClient.inquiryRentTransaction(
                    districtCodeFirst5, dealYmd, rentHousingType);
            List<RentTransactionApiResponse.ApiResponseItem> items = extractTransactionItems(apiResponse);

            return items.stream()
                    .filter(item -> item.getUmdNm().equals(dongName) && item.getJibun().equals(jibun))
                    .map(this::convertToTransactionDetail);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch rent transactions for the month: " + dealYmd, e);
        }
    }

    /**
     * 거래 항목을 TransactionDetail 객체로 변환합니다.
     *
     * @param item 거래 항목
     * @return TransactionDetail 객체
     */
    private RentTransactionInquiryResponse.TransactionDetail convertToTransactionDetail(
            RentTransactionApiResponse.ApiResponseItem item) {
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
     * 주택 유형 코드를 RentHousingType으로 매핑합니다.
     *
     * @param rentHousingTypeCode 주택 유형 코드
     * @return RentHousingType 열거형 값
     */
    private RentHousingType mapRentHousingType(String rentHousingTypeCode) {
        switch (Integer.parseInt(rentHousingTypeCode)) {
            case 1:
                return RentHousingType.APARTMENT;
            case 2:
                return RentHousingType.OFFICETEL;
            case 3:
            case 4:
                return RentHousingType.HOUSEHOLD_HOUSE;
            case 5:
            case 6:
                return RentHousingType.FAMILY_HOUSE;
            default:
                throw new IllegalArgumentException("Unknown Rent Housing Type Code: " + rentHousingTypeCode);
        }
    }

    /**
     * 지번을 파싱하여 메인 지번과 서브 지번으로 분리합니다.
     *
     * @param jibun 지번 문자열
     * @return 메인 지번과 서브 지번의 배열
     */
    public static String[] parseJibun(String jibun) {
        String[] parts = jibun.split("-");
        String main = formatJibunPart(parts[0]);
        String sub = (parts.length > 1) ? formatJibunPart(parts[1]) : "0000";
        return new String[]{main, sub};
    }

    /**
     * 지번의 특정 부분을 4자리 형식으로 변환합니다.
     *
     * @param part 지번의 부분 문자열
     * @return 4자리로 포맷된 지번 부분
     */
    private static String formatJibunPart(String part) {
        return String.format("%04d", Integer.parseInt(part));
    }

    /**
     * 전용 면적을 소수점 둘째 자리까지 포맷된 문자열로 변환합니다.
     *
     * @param exclusiveArea 전용 면적
     * @return 소수점 둘째 자리까지 포맷된 문자열
     */
    private String formatExclusiveArea(double exclusiveArea) {
        return String.format("%.2f", exclusiveArea);
    }

    /**
     * API 응답에서 거래 항목 목록을 추출합니다.
     *
     * @param apiResponse API 응답 맵 객체
     * @return 거래 항목 목록
     */
    @SuppressWarnings("unchecked")
    private List<RentTransactionApiResponse.ApiResponseItem> extractTransactionItems(Map<String, Object> apiResponse) {
        return (List<RentTransactionApiResponse.ApiResponseItem>)
                apiResponse.getOrDefault("rentTransactionInfoList", List.of());
    }

    /**
     * 요청된 개월 수에 따른 날짜 범위를 생성합니다.
     *
     * @param currentDate 기준 날짜
     * @param months      개월 수
     * @return 날짜 문자열의 스트림
     */
    private Stream<String> generateDateRange(LocalDate currentDate, int months) {
        return currentDate.minusMonths(BASE_MONTH_OFFSET)
                .minusMonths(months - 1)
                .datesUntil(currentDate.minusMonths(BASE_MONTH_OFFSET).plusMonths(1), java.time.Period.ofMonths(1))
                .map(date -> date.format(DATE_FORMATTER));
    }
}
