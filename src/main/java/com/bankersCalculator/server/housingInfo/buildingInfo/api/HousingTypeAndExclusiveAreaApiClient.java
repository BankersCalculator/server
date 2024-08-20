package com.bankersCalculator.server.housingInfo.buildingInfo.api;

import com.bankersCalculator.server.housingInfo.buildingInfo.dto.HousingTypeAndExclusiveAreaApiResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 국토교통부 주택 유형 및 전용 면적 조회 API
 * @param districtCodeFirst5 시군구 코드 앞 5자리 (예: "11110" 서울의 특정 지역 코드)
 * @param districtCodeLast5 법정동 코드 뒤 5자리 (예: "10300" 특정 읍면동 코드)
 * @param landDivisionCode 지목 코드 (예: "1" 대지)
 * @param jibunMain 지번 본번 (예: "0021")
 * @param jibunSub 지번 부번 (예: "0000")
 * @param numOfRows 조회할 행 수 (예: "10")
 * @param pageNo 페이지 번호 (예: "1")
 * @return 주택 유형 및 전용 면적 정보를 포함하는 {@link HousingTypeAndExclusiveAreaApiResponse} 객체.
 *         이 객체에는 다음과 같은 항목들이 포함됩니다:
 *         - "header": API 응답 결과 코드와 메시지를 포함하는 {@link HousingTypeAndExclusiveAreaApiResponse.ApiResponseHeader} 객체
 *         - "body": 주택 유형 및 전용 면적 정보를 포함하는 {@link HousingTypeAndExclusiveAreaApiResponse.ApiResponseBody} 객체
 *           - "items": 주택 유형 및 전용 면적 항목들을 포함하는 리스트. 각 항목은 {@link HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem} 객체로 이루어져 있으며,
 *             각 객체는 다음과 같은 세부 정보를 포함합니다:
 *               - "rentHousingTypeCode": 주택 유형 코드
 *               - "rentHousingTypeName": 주택 유형 이름
 *               - "houseHoldCount": 세대 수
 *               - "exclusiveArea": 전용 면적
 * @throws RuntimeException API 호출 또는 응답 처리 중 오류가 발생한 경우
 */
@Service
public class HousingTypeAndExclusiveAreaApiClient {
    private static final Logger logger = LoggerFactory.getLogger(HousingTypeAndExclusiveAreaApiClient.class);
    private final XmlMapper xmlMapper;

    public HousingTypeAndExclusiveAreaApiClient() {
        this.xmlMapper = new XmlMapper();
    }

    public HousingTypeAndExclusiveAreaApiResponse getApHsTpInfo(
            String districtCodeFirst5,
            String districtCodeLast5,
            String jibunMain,
            String jibunSub
            ) {

        String serviceKey = URLEncoder.encode("EOKGlRDintEZcHeH5tnMFZhkvd1WGjIPchUi1RHeI3pxw5e2y196Cijpr2zTwTZ4QnDJ7+pW3J2FnyVXkFfXTA==", StandardCharsets.UTF_8);
        String apiUrl = UriComponentsBuilder.fromHttpUrl("https://apis.data.go.kr/1613000/ArchPmsService_v2/getApHsTpInfo")
                .queryParam("serviceKey", serviceKey)
                .queryParam("sigunguCd", districtCodeFirst5)
                .queryParam("bjdongCd", districtCodeLast5)
                .queryParam("platGbCd", "0")
                .queryParam("bun", jibunMain)
                .queryParam("ji", jibunSub)
                .queryParam("numOfRows", "20")
                .queryParam("pageNo", "1")
                .build()
                .toUriString();

        logger.info("Calling API with URL: {}", apiUrl);

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            JSONObject jsonObject = new JSONObject(xmlMapper.readTree(sb.toString()).toString());
            return processApiResponse(jsonObject);

        } catch (Exception e) {
            logger.error("Error occurred while calling the API", e);
            throw new RuntimeException("Error occurred while calling the API", e);
        }
    }

    private HousingTypeAndExclusiveAreaApiResponse processApiResponse(JSONObject jsonObject) {
        HousingTypeAndExclusiveAreaApiResponse response = new HousingTypeAndExclusiveAreaApiResponse();

        JSONObject header = jsonObject.getJSONObject("header");
        HousingTypeAndExclusiveAreaApiResponse.ApiResponseHeader responseHeader = new HousingTypeAndExclusiveAreaApiResponse.ApiResponseHeader();
        responseHeader.setResultCode(header.getString("resultCode"));
        responseHeader.setResultMsg(header.getString("resultMsg"));
        response.setHeader(responseHeader);

        JSONObject body = jsonObject.getJSONObject("body");
        HousingTypeAndExclusiveAreaApiResponse.ApiResponseBody responseBody = new HousingTypeAndExclusiveAreaApiResponse.ApiResponseBody();

        if (!body.isNull("items")) {
            JSONArray itemsArray = body.getJSONObject("items").getJSONArray("item");
            List<HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem> itemList = new ArrayList<>();
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemObject = itemsArray.getJSONObject(i);
                HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem item = new HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem();
                item.setRentHousingTypeCode(itemObject.getString("hstpGbCd"));
                item.setRentHousingTypeName(itemObject.getString("hstpGbCdNm"));
                item.setHouseHoldCount(itemObject.getInt("silHoHhldCnt"));
                item.setExclusiveArea(itemObject.getDouble("silHoHhldArea"));

                itemList.add(item);
            }
            responseBody.setItems(new HousingTypeAndExclusiveAreaApiResponse.ApiResponseItems());
            responseBody.getItems().setItemList(itemList);
        }

        responseBody.setNumOfRows(body.getInt("numOfRows"));
        responseBody.setPageNo(body.getInt("pageNo"));
        responseBody.setTotalCount(body.getInt("totalCount"));
        response.setBody(responseBody);

        return response;
    }
}
