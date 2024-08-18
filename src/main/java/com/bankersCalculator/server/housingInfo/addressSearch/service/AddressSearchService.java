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

    private String callJusoApi(String keyword) throws IOException {
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

        return sb.toString();
    }

    private List<AddressSearchApiResponse> parseJusoApiResponse(String apiResponse) throws JSONException {
        List<AddressSearchApiResponse> addressList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(apiResponse);

        JSONObject common = jsonObject.getJSONObject("results").getJSONObject("common");
        String errorCode = common.getString("errorCode");

        if ("0".equals(errorCode) && !jsonObject.getJSONObject("results").isNull("juso")) {
            JSONArray jusoArray = jsonObject.getJSONObject("results").getJSONArray("juso");

            for (int i = 0; i < jusoArray.length(); i++) {
                JSONObject addressObject = jusoArray.getJSONObject(i);
                AddressSearchApiResponse addressSearchApiResponse = new AddressSearchApiResponse();
                addressSearchApiResponse.setRoadAddress(addressObject.getString("roadAddr"));
                addressSearchApiResponse.setJibunAddress(addressObject.getString("jibunAddr"));
                addressSearchApiResponse.setDistrictCode(addressObject.getString("admCd"));
                addressSearchApiResponse.setBuildingName(addressObject.getString("bdNm"));
                addressSearchApiResponse.setDongName(addressObject.getString("emdNm"));

                int jibunMain = addressObject.getInt("lnbrMnnm");
                int jibunSub = addressObject.getInt("lnbrSlno");

                String jibun = jibunMain + "-" + jibunSub;

                addressSearchApiResponse.setJibun(jibun);
                addressList.add(addressSearchApiResponse);
            }
        }

        return addressList;
    }

    public Map<String, Object> searchAddress(String keyword) throws IOException, JSONException {
        String apiResponse = callJusoApi(keyword);
        JSONObject jsonObject = new JSONObject(apiResponse);

        JSONObject common = jsonObject.getJSONObject("results").getJSONObject("common");
        String errorCode = common.getString("errorCode");
        String errorMessage = common.getString("errorMessage");

        Map<String, Object> searchResult = new HashMap<>();
        searchResult.put("errorCode", errorCode);
        searchResult.put("errorMessage", errorMessage);

        if ("0".equals(errorCode)) {
            List<AddressSearchApiResponse> addressList = parseJusoApiResponse(apiResponse);
            searchResult.put("addressList", addressList);
        } else {
            searchResult.put("addressList", new ArrayList<>());
        }

        return searchResult;
    }
}
