package com.bankersCalculator.server.addressSearch.service;
import com.bankersCalculator.server.housingInfo.addressSearch.dto.AddressSearchResponse;
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
        String keyword0 = "삼성동 189";
        String keyword1 = " ";
        String keyword2 = "서";
        String keyword3 = "123";
        String keyword4 = "한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글한글";
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
        List<AddressSearchResponse> addressList = (List<AddressSearchResponse>) result0.get("jusoList");
        AddressSearchResponse firstAddress = addressList.get(0);
        System.out.println("도로명 주소: " + firstAddress.getRoadAddr());
        System.out.println("지번 주소: " + firstAddress.getJibunAddr());
        System.out.println("우편번호: " + firstAddress.getZipNo());
        System.out.println("행정구역 코드: " + firstAddress.getAdmCd());
        System.out.println("도로명 코드: " + firstAddress.getRnMgtSn());
        System.out.println("건물 관리 코드: " + firstAddress.getBdMgtSn());
        System.out.println("상세 건물명: " + firstAddress.getDetBdNmList());
        System.out.println("건물명: " + firstAddress.getBdNm());
        System.out.println("공동주택 여부: " + firstAddress.getBdKdcd());
        System.out.println("시도명: " + firstAddress.getSiNm());
        System.out.println("시군구명: " + firstAddress.getSggNm());
        System.out.println("읍면동명: " + firstAddress.getEmdNm());
        System.out.println("법정리명: " + firstAddress.getLiNm());
        System.out.println("도로명: " + firstAddress.getRn());
        System.out.println("지하 여부: " + firstAddress.getUdrtYn());
        System.out.println("건물 본번: " + firstAddress.getBuldMnnm());
        System.out.println("건물 부번: " + firstAddress.getBuldSlno());
        System.out.println("산 여부: " + firstAddress.getMtYn());
        System.out.println("지번 본번: " + firstAddress.getLnbrMnnm());
        System.out.println("지번 부번: " + firstAddress.getLnbrSlno());
        System.out.println("읍면동 일련 번호: " + firstAddress.getEmdNo());




        String errorCode0 = (String) result0.get("errorCode");
        String errorCode1 = (String) result1.get("errorCode");
        String errorCode2 = (String) result2.get("errorCode");
        String errorCode3 = (String) result3.get("errorCode");
        String errorCode4 = (String) result4.get("errorCode");
        String errorCode5 = (String) result5.get("errorCode");
        String errorCode6 = (String) result6.get("errorCode");
        String errorCode7 = (String) result7.get("errorCode");

        String errorMessage0 = (String) result0.get("errorMessage");
        String errorMessage1 = (String) result1.get("errorMessage");
        String errorMessage2 = (String) result2.get("errorMessage");
        String errorMessage3 = (String) result3.get("errorMessage");
        String errorMessage4 = (String) result4.get("errorMessage");
        String errorMessage5 = (String) result5.get("errorMessage");
        String errorMessage6 = (String) result6.get("errorMessage");
        String errorMessage7 = (String) result7.get("errorMessage");


        System.out.println("Error Code0: " + errorCode0 + ", Error Message0: " + errorMessage0);
        System.out.println("Error Code1: " + errorCode1 + ", Error Message1: " + errorMessage1);
        System.out.println("Error Code2: " + errorCode2 + ", Error Message2: " + errorMessage2);
        System.out.println("Error Code3: " + errorCode3 + ", Error Message3: " + errorMessage3);
        System.out.println("Error Code4: " + errorCode4 + ", Error Message4: " + errorMessage4);
        System.out.println("Error Code5: " + errorCode5 + ", Error Message5: " + errorMessage5);
        System.out.println("Error Code6: " + errorCode6 + ", Error Message6: " + errorMessage6);
        System.out.println("Error Code7: " + errorCode7 + ", Error Message7: " + errorMessage7);

        assertEquals("E0005", errorCode1 );
        assertEquals("E0008", errorCode2  );
        assertEquals("E0009", errorCode3 );
        assertEquals("E0010", errorCode4  );
        assertEquals("E0011", errorCode5 );
        assertEquals("E0012", errorCode6 );
        assertEquals("E0013", errorCode7 );
    }
}
