package com.bankersCalculator.server.housingInfo.addressSearch.service;

import com.bankersCalculator.server.housingInfo.addressSearch.dto.AddressSearchApiResponse;
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

@Service
@RequiredArgsConstructor
public class AddressSearchService {

    private static final String API_URL = "https://business.juso.go.kr/addrlink/addrLinkApi.do";
    private static final String API_KEY = "devU01TX0FVVEgyMDI0MDgwNTIxMTIzNjExNDk4OTM=";
    private static final String CURRENT_PAGE = "1";
    private static final String COUNT_PER_PAGE = "10";
    private static final String RESULT_TYPE = "json";

    private String buildApiUrl(String keyword) throws IOException {
        return API_URL
                + "?confmKey=" + API_KEY
                + "&currentPage=" + CURRENT_PAGE
                + "&countPerPage=" + COUNT_PER_PAGE
                + "&resultType=" + RESULT_TYPE
                + "&keyword=" + URLEncoder.encode(keyword, "UTF-8");
    }

    private String callAddressSerarchApi(String apiUrl) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        }
    }

    private List<AddressSearchApiResponse> parseAddressInfoResponse(String apiResponse) throws JSONException {
        List<AddressSearchApiResponse> addressInfoList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(apiResponse);
        JSONObject results = jsonObject.getJSONObject("results");
        JSONObject common = results.getJSONObject("common");

        if ("0".equals(common.getString("errorCode")) && !results.isNull("juso")) {
            JSONArray jusoArray = results.getJSONArray("juso");
            for (int i = 0; i < jusoArray.length(); i++) {
                JSONObject addressObject = jusoArray.getJSONObject(i);
                AddressSearchApiResponse addressSearchApiResponse = new AddressSearchApiResponse();
                addressSearchApiResponse.setRoadAddress(addressObject.getString("roadAddr"));
                addressSearchApiResponse.setJibunAddress(addressObject.getString("jibunAddr"));
                addressSearchApiResponse.setDistrictCode(addressObject.getString("admCd"));
                addressSearchApiResponse.setBuildingName(addressObject.getString("bdNm"));
                addressSearchApiResponse.setDongName(addressObject.getString("emdNm"));
                addressSearchApiResponse.setJibun(addressObject.getInt("lnbrMnnm") + "-" + addressObject.getInt("lnbrSlno"));

                addressInfoList.add(addressSearchApiResponse);
            }
        }
        return addressInfoList;
    }

    public Map<String, Object> searchAddress(String keyword) throws IOException, JSONException {
        String apiUrl = buildApiUrl(keyword);
        String apiResponse = callAddressSerarchApi(apiUrl);
        JSONObject jsonObject = new JSONObject(apiResponse);
        JSONObject common = jsonObject.getJSONObject("results").getJSONObject("common");

        Map<String, Object> searchResult = new HashMap<>();
        searchResult.put("apiResultCode", common.getString("errorCode"));
        searchResult.put("apiResultMessage", common.getString("errorMessage"));

        List<AddressSearchApiResponse> addressInfoList = parseAddressInfoResponse(apiResponse);
        searchResult.put("addressInfoList", addressInfoList);

        return searchResult;
    }
}
