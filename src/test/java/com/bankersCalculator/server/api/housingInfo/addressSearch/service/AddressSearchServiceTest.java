package com.bankersCalculator.server.api.housingInfo.addressSearch.service;

import com.bankersCalculator.server.housingInfo.addressSearch.dto.AddressSearchApiResponse;
import com.bankersCalculator.server.housingInfo.addressSearch.service.AddressSearchService;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AddressSearchServiceTest {

    @Test
    void testSearchAddress() throws IOException, JSONException {
        AddressSearchService addressSearchService = new AddressSearchService();

        // 사용할 키워드 설정
        String keyword0 = "청라한내로 100번길";
        String keyword1 = " ";
        String keyword2 = "서";
        String keyword3 = "123";
        String keyword4 = "한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글";
        String keyword5 = "12345678901222이";
        String keyword6 = "##";
        String keyword7 = "SELECT";

        // 실제 API 호출
        Map<String, Object> result0 = addressSearchService.searchAddress(keyword0);
        Map<String, Object> result1 = addressSearchService.searchAddress(keyword1);
        Map<String, Object> result2 = addressSearchService.searchAddress(keyword2);
        Map<String, Object> result3 = addressSearchService.searchAddress(keyword3);
        Map<String, Object> result4 = addressSearchService.searchAddress(keyword4);
        Map<String, Object> result5 = addressSearchService.searchAddress(keyword5);
        Map<String, Object> result6 = addressSearchService.searchAddress(keyword6);
        Map<String, Object> result7 = addressSearchService.searchAddress(keyword7);

        // 결과 검증
        @SuppressWarnings("unchecked")
        List<AddressSearchApiResponse> addressInfoList = (List<AddressSearchApiResponse>) result0.get("addressInfoList");
        AddressSearchApiResponse firstAddress = addressInfoList.get(0);

        // 검증 로직
//        System.out.println("도로명 주소: " + firstAddress.getRoadAddress());
//        System.out.println("지번 주소: " + firstAddress.getJibunAddress());
//        System.out.println("행정구역 코드: " + firstAddress.getDistrictCode());
//        System.out.println("건물명: " + firstAddress.getBuildingName());
//        System.out.println("읍면동명: " + firstAddress.getDongName());
//        System.out.println("지번(본번- 부번): " + firstAddress.getJibun());


        // 에러 코드 및 메시지 검증
        String apiResultCode0 = (String) result0.get("apiResultCode");
        String apiResultCode1 = (String) result1.get("apiResultCode");
        String apiResultCode2 = (String) result2.get("apiResultCode");
        String apiResultCode3 = (String) result3.get("apiResultCode");
        String apiResultCode4 = (String) result4.get("apiResultCode");
        String apiResultCode5 = (String) result5.get("apiResultCode");
        String apiResultCode6 = (String) result6.get("apiResultCode");
        String apiResultCode7 = (String) result7.get("apiResultCode");

        String apiResultMessage0 = (String) result0.get("apiResultMessage");
        String apiResultMessage1 = (String) result1.get("apiResultMessage");
        String apiResultMessage2 = (String) result2.get("apiResultMessage");
        String apiResultMessage3 = (String) result3.get("apiResultMessage");
        String apiResultMessage4 = (String) result4.get("apiResultMessage");
        String apiResultMessage5 = (String) result5.get("apiResultMessage");
        String apiResultMessage6 = (String) result6.get("apiResultMessage");
        String apiResultMessage7 = (String) result7.get("apiResultMessage");

//        System.out.println("Error Code0: " + apiResultCode0 + ", Error Message0: " + apiResultMessage0);
//        System.out.println("Error Code1: " + apiResultCode1 + ", Error Message1: " + apiResultMessage1);
//        System.out.println("Error Code2: " + apiResultCode2 + ", Error Message2: " + apiResultMessage2);
//        System.out.println("Error Code3: " + apiResultCode3 + ", Error Message3: " + apiResultMessage3);
//        System.out.println("Error Code4: " + apiResultCode4 + ", Error Message4: " + apiResultMessage4);
//        System.out.println("Error Code5: " + apiResultCode5 + ", Error Message5: " + apiResultMessage5);
//        System.out.println("Error Code6: " + apiResultCode6 + ", Error Message6: " + apiResultMessage6);
//        System.out.println("Error Code7: " + apiResultCode7 + ", Error Message7: " + apiResultMessage7);

        assertEquals("E0005", apiResultCode1);
        assertEquals("E0008", apiResultCode2);
        assertEquals("E0009", apiResultCode3);
        assertEquals("E0010", apiResultCode4);
        assertEquals("E0011", apiResultCode5);
        assertEquals("E0012", apiResultCode6);
        assertEquals("E0013", apiResultCode7);
    }
}
