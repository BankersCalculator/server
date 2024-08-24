package com.bankersCalculator.server.housingInfo.buildingInfo.api;

import com.bankersCalculator.server.housingInfo.buildingInfo.common.RentHousingType;
import com.bankersCalculator.server.housingInfo.buildingInfo.config.RentTransactionApiConfig;
import com.bankersCalculator.server.housingInfo.buildingInfo.dto.RentTransactionApiResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 국토교통부 전/월세 실거래가 조회 API
 * @param districtCodeFirst5 법정동 코드 (예: "11110" 서울의 특정 지역 코드)
 * @param dealYmd 거래 연월 (형식: "YYYYMM", 예: "202407")
 * @param rentHousingType 조회할 주택 유형 (예: APARTMENT, OFFICETEL)
 * @return 임대 거래 정보를 포함하는 {@link RentTransactionApiResponse} 객체.
 *         이 객체에는 다음과 같은 항목들이 포함됩니다:
 *         - "header": API 응답 결과 코드와 메시지를 포함하는 {@link RentTransactionApiResponse.ApiResponseHeader} 객체
 *         - "body": 임대 거래 세부 정보를 포함하는 {@link RentTransactionApiResponse.ApiResponseBody} 객체
 *           - "items": 임대 거래 항목들을 포함하는 리스트. 각 항목은 {@link RentTransactionApiResponse.ApiResponseItem} 객체로 이루어져 있으며,
 *             각 객체는 다음과 같은 세부 정보를 포함합니다:
 *               - "aptNm": 아파트 이름
 *               - "buildYear": 건축 연도
 *               - "contractTerm": 계약 기간
 *               - "contractType": 계약 유형
 *               - "dealDay": 거래일
 *               - "dealMonth": 거래월
 *               - "dealYear": 거래연도
 *               - "deposit": 보증금
 *               - "excluUseAr": 전용면적
 *               - "floor": 층수
 *               - "jibun": 지번
 *               - "monthlyRent": 월세 금액
 *               - "preDeposit": 이전 보증금
 *               - "preMonthlyRent": 이전 월세 금액
 *               - "sggCd": 시군구 코드
 *               - "umdNm": 읍면동 이름
 *               - "useRRRight": 사용 승락 권리
 * @throws RuntimeException API 호출 또는 응답 처리 중 오류가 발생한 경우
 */
@Service
public class RentTransactionApiClient {
    private static final Logger logger = LoggerFactory.getLogger(RentTransactionApiClient.class);
    private final RentTransactionApiConfig apiConfig;
    private final XmlMapper xmlMapper;

    public RentTransactionApiClient(RentTransactionApiConfig apiConfig ) {
        this.apiConfig = apiConfig;
        this.xmlMapper = new XmlMapper();
    }

    public RentTransactionApiResponse RentTransactionCallApi(String districtCodeFirst5, String dealYmd, RentHousingType rentHousingType) throws IOException {
        String apiUrl = apiConfig.getFullApiUrl(districtCodeFirst5, dealYmd, rentHousingType);
        logger.info("Calling API with URL: {}", apiUrl);

        try {
            // HTTP 요청 설정
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 응답 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            // XML을 JSON으로 변환
            JSONObject jsonObject = new JSONObject(xmlMapper.readTree(sb.toString()).toString());
            //logger.info("Converted XML to JSON: {}", jsonObject);

            // 결과 처리 및 DTO 매핑
            return processApiResponse(jsonObject);

        } catch (Exception e) {
            logger.error("Error occurred while calling the API", e);
            throw new RuntimeException("Error occurred while calling the API", e);
        }
    }

    private RentTransactionApiResponse processApiResponse(JSONObject jsonObject) {
        RentTransactionApiResponse response = new RentTransactionApiResponse();

        JSONObject header = jsonObject.getJSONObject("header");
        RentTransactionApiResponse.ApiResponseHeader responseHeader = new RentTransactionApiResponse.ApiResponseHeader();
        responseHeader.setResultCode(header.getString("resultCode"));
        responseHeader.setResultMsg(header.getString("resultMsg"));
        response.setHeader(responseHeader);

        JSONObject body = jsonObject.getJSONObject("body");
        RentTransactionApiResponse.ApiResponseBody responseBody = new RentTransactionApiResponse.ApiResponseBody();

        if (!body.isNull("items")) {
            JSONArray itemsArray = body.getJSONObject("items").getJSONArray("item");
            List<RentTransactionApiResponse.ApiResponseItem> itemList = new ArrayList<>();
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);
                RentTransactionApiResponse.ApiResponseItem item = new RentTransactionApiResponse.ApiResponseItem();
                item.setAptNm(itemObject.has("aptNm") ? itemObject.getString("aptNm") : null);
                item.setBuildYear(itemObject.has("buildYear") ? itemObject.getString("buildYear") : null);
                item.setContractTerm(itemObject.has("contractTerm") ? itemObject.getString("contractTerm") : null);
                item.setContractType(itemObject.has("contractType") ? itemObject.getString("contractType") : null);
                item.setDealDay(itemObject.has("dealDay") ? itemObject.getString("dealDay") : null);
                item.setDealMonth(itemObject.has("dealMonth") ? itemObject.getString("dealMonth") : null);
                item.setDealYear(itemObject.has("dealYear") ? itemObject.getString("dealYear") : null);
                item.setDeposit(itemObject.has("deposit") ? itemObject.getString("deposit") : null);
                item.setExcluUseAr(itemObject.has("excluUseAr") ? itemObject.getString("excluUseAr") : null);
                item.setFloor(itemObject.has("floor") ? itemObject.getString("floor") : null);
                item.setJibun(itemObject.has("jibun") ? itemObject.getString("jibun") : null);
                item.setMonthlyRent(itemObject.has("monthlyRent") ? itemObject.getString("monthlyRent") : null);
                item.setPreDeposit(itemObject.has("preDeposit") ? itemObject.getString("preDeposit") : null);
                item.setPreMonthlyRent(itemObject.has("preMonthlyRent") ? itemObject.getString("preMonthlyRent") : null);
                item.setSggCd(itemObject.has("sggCd") ? itemObject.getString("sggCd") : null);
                item.setUmdNm(itemObject.has("umdNm") ? itemObject.getString("umdNm") : null);
                item.setUseRRRight(itemObject.has("useRRRight") ? itemObject.getString("useRRRight") : null);

                itemList.add(item);
            }
            responseBody.setItems(new RentTransactionApiResponse.ApiResponseItems());
            responseBody.getItems().setItemList(itemList);
        }

        response.setBody(responseBody);
        return response;
    }
}
