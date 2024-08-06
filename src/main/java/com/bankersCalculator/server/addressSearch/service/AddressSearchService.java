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
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressSearchService {
    public List<AddressResponse> searchAddress(String keyword) throws IOException, JSONException {
        String apiUrl = "https://business.juso.go.kr/addrlink/addrLinkApi.do";
        String apiKey = "devU01TX0FVVEgyMDI0MDgwNTIxMTIzNjExNDk4OTM=";
        String currentPage = "1"; //현재페이지번호
        String countPerPage = "10"; //페이지당 출력할 Row 수
        String resultType = "json"; //검색결과형 설정 (xml/json)
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

        System.out.println(sb);

        JSONObject jsonObject = new JSONObject(sb.toString());
        JSONArray jusoArray = jsonObject.getJSONObject("results").getJSONArray("juso");

        List<AddressResponse> addressList = new ArrayList<>();
        for (int i = 0; i < jusoArray.length(); i++) {
            JSONObject jusoObject = jusoArray.getJSONObject(i);
            AddressResponse addressResponse = new AddressResponse();
            addressResponse.setRoadAddr(jusoObject.getString("roadAddr"));
            addressResponse.setJibunAddr(jusoObject.getString("jibunAddr"));
            addressResponse.setZipNo(jusoObject.getString("zipNo"));
            addressResponse.setAdmCd(jusoObject.getString("admCd"));
            addressResponse.setRnMgtSn(jusoObject.getString("rnMgtSn"));
            addressResponse.setBdMgtSn(jusoObject.getString("bdMgtSn"));
            addressResponse.setDetBdNmList(jusoObject.getString("detBdNmList"));
            addressResponse.setBdNm(jusoObject.getString("bdNm"));
            addressResponse.setBdKdcd(jusoObject.getString("bdKdcd"));
            addressResponse.setSiNm(jusoObject.getString("siNm"));
            addressResponse.setSggNm(jusoObject.getString("sggNm"));
            addressResponse.setEmdNm(jusoObject.getString("emdNm"));
            addressResponse.setLiNm(jusoObject.getString("liNm"));
            addressResponse.setRn(jusoObject.getString("rn"));
            addressResponse.setUdrtYn(jusoObject.getString("udrtYn"));
            addressResponse.setBuldMnnm(jusoObject.getInt("buldMnnm"));
            addressResponse.setBuldSlno(jusoObject.getInt("buldSlno"));
            addressResponse.setMtYn(jusoObject.getString("mtYn"));
            addressResponse.setLnbrMnnm(jusoObject.getInt("lnbrMnnm"));
            addressResponse.setLnbrSlno(jusoObject.getInt("lnbrSlno"));
            addressResponse.setEmdNo(jusoObject.getString("emdNo"));
            addressList.add(addressResponse);
        }
        return addressList;
    }
}
