package com.bankersCalculator.server.housingInfo.addressSearch.service;

import com.bankersCalculator.server.housingInfo.addressSearch.dto.AddressSearchResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 주어진 키워드를 사용하여 주소 검색 이 메서드는 Juso API를 호출하여 주소 정보를 가져옵니다.
 *
 * @param keyword 주소를 검색 키워드
 * @return 검색 결과를 포함하는 Map 객체입니다. 이 Map에는 다음과 같은 항목들이 포함됩니다:
 *         - "errorCode": API 요청 결과
 *         - "errorMessage": 결과에 대한 메시지
 *         - "jusoList": 주소 세부 정보를 포함하는 {@link AddressSearchResponse} 객체 리스트. 주소가 없을 경우 빈 리스트 반환
 * @throws IOException 입출력 예외
 * @throws JSONException API로부터 받은 JSON 응답을 파싱하는 동안 오류가 발생한 경우
 */

@Service
@RequiredArgsConstructor
public class AddressSearchService {
    public Map<String, Object> searchAddress(String keyword) throws IOException, JSONException {
        String apiUrl = "https://business.juso.go.kr/addrlink/addrLinkApi.do";
        String apiKey = "devU01TX0FVVEgyMDI0MDgwNTIxMTIzNjExNDk4OTM=";
        String currentPage = "1";
        String countPerPage = "10";
        String resultType = "json";
        String apiUrlWithParams = apiUrl
                + "?confmKey=" + apiKey
                + "&currentPage=" + currentPage
                + "&countPerPage=" + countPerPage
                + "&resultType=" + resultType
                + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");

        URL url = new URL(apiUrlWithParams);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        JSONObject jsonObject = new JSONObject(sb.toString());

        JSONObject common = jsonObject.getJSONObject("results").getJSONObject("common");
        String errorCode = common.getString("errorCode");
        String errorMessage = common.getString("errorMessage");

        Map<String, Object> searchResult = new HashMap<>();
        searchResult.put("errorCode", errorCode);
        searchResult.put("errorMessage", errorMessage);


        if (!jsonObject.getJSONObject("results").isNull("juso")) {
            JSONArray jusoArray = jsonObject.getJSONObject("results").getJSONArray("juso");
            List<AddressSearchResponse> addressList = new ArrayList<>();

            for (int i = 0; i < jusoArray.length(); i++) {
                JSONObject jusoObject = jusoArray.getJSONObject(i);
                AddressSearchResponse addressSearchResponse = new AddressSearchResponse();
                addressSearchResponse.setRoadAddr(jusoObject.getString("roadAddr"));
                addressSearchResponse.setJibunAddr(jusoObject.getString("jibunAddr"));
                addressSearchResponse.setZipNo(jusoObject.getString("zipNo"));
                addressSearchResponse.setAdmCd(jusoObject.getString("admCd"));
                addressSearchResponse.setRnMgtSn(jusoObject.getString("rnMgtSn"));
                addressSearchResponse.setBdMgtSn(jusoObject.getString("bdMgtSn"));
                addressSearchResponse.setDetBdNmList(jusoObject.getString("detBdNmList"));
                addressSearchResponse.setBdNm(jusoObject.getString("bdNm"));
                addressSearchResponse.setBdKdcd(jusoObject.getString("bdKdcd"));
                addressSearchResponse.setSiNm(jusoObject.getString("siNm"));
                addressSearchResponse.setSggNm(jusoObject.getString("sggNm"));
                addressSearchResponse.setEmdNm(jusoObject.getString("emdNm"));
                addressSearchResponse.setLiNm(jusoObject.getString("liNm"));
                addressSearchResponse.setRn(jusoObject.getString("rn"));
                addressSearchResponse.setUdrtYn(jusoObject.getString("udrtYn"));
                addressSearchResponse.setBuldMnnm(jusoObject.getInt("buldMnnm"));
                addressSearchResponse.setBuldSlno(jusoObject.getInt("buldSlno"));
                addressSearchResponse.setMtYn(jusoObject.getString("mtYn"));
                addressSearchResponse.setLnbrMnnm(jusoObject.getInt("lnbrMnnm"));
                addressSearchResponse.setLnbrSlno(jusoObject.getInt("lnbrSlno"));
                addressSearchResponse.setEmdNo(jusoObject.getString("emdNo"));
                addressList.add(addressSearchResponse);
            }
            searchResult.put("jusoList", addressList);
        } else {
            // juso가 없는 경우 빈 리스트 반환
            searchResult.put("jusoList", new ArrayList<>());
        }

        return searchResult;
    }
}
