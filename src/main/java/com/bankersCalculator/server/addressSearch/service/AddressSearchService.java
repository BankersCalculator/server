package com.bankersCalculator.server.addressSearch.service;

import com.bankersCalculator.server.addressSearch.dto.AddressResponse;
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

        JSONObject jusoObject = jsonObject.getJSONObject("results").optJSONObject("juso");
        if (jusoObject != null) {
            List<AddressResponse> addressList = new ArrayList<>();
            AddressResponse addressResponse = new AddressResponse();
            addressResponse.setRoadAddr(jusoObject.optString("roadAddr"));
            addressResponse.setJibunAddr(jusoObject.optString("jibunAddr"));
            addressResponse.setZipNo(jusoObject.optString("zipNo"));
            addressResponse.setAdmCd(jusoObject.optString("admCd"));
            addressResponse.setRnMgtSn(jusoObject.optString("rnMgtSn"));
            addressResponse.setBdMgtSn(jusoObject.optString("bdMgtSn"));
            addressResponse.setDetBdNmList(jusoObject.optString("detBdNmList"));
            addressResponse.setBdNm(jusoObject.optString("bdNm"));
            addressResponse.setBdKdcd(jusoObject.optString("bdKdcd"));
            addressResponse.setSiNm(jusoObject.optString("siNm"));
            addressResponse.setSggNm(jusoObject.optString("sggNm"));
            addressResponse.setEmdNm(jusoObject.optString("emdNm"));
            addressResponse.setLiNm(jusoObject.optString("liNm"));
            addressResponse.setRn(jusoObject.optString("rn"));
            addressResponse.setUdrtYn(jusoObject.optString("udrtYn"));
            addressResponse.setBuldMnnm(jusoObject.optInt("buldMnnm"));
            addressResponse.setBuldSlno(jusoObject.optInt("buldSlno"));
            addressResponse.setMtYn(jusoObject.optString("mtYn"));
            addressResponse.setLnbrMnnm(jusoObject.optInt("lnbrMnnm"));
            addressResponse.setLnbrSlno(jusoObject.optInt("lnbrSlno"));
            addressResponse.setEmdNo(jusoObject.optString("emdNo"));
            addressList.add(addressResponse);
            searchResult.put("jusoList", addressList);
        } else {
            searchResult.put("jusoList", new ArrayList<>());
        }

        return searchResult;
    }
}
