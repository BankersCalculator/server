package com.myZipPlan.server.housingInfo.buildingInfo.config;
import com.myZipPlan.server.housingInfo.buildingInfo.common.RentHousingType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class RentTransactionApiConfig {
    private final String endPointUrl = "https://apis.data.go.kr/1613000/";
    private String serviceKey = "EOKGlRDintEZcHeH5tnMFZhkvd1WGjIPchUi1RHeI3pxw5e2y196Cijpr2zTwTZ4QnDJ7+pW3J2FnyVXkFfXTA==";
    public String endpointMethod(RentHousingType rentHousingType) {
        switch (rentHousingType) {
            case APARTMENT:
                return "RTMSDataSvcAptRent/getRTMSDataSvcAptRent?";
            case OFFICETEL:
                return "RTMSDataSvcOffiRent/getRTMSDataSvcOffiRent?";
            case HOUSEHOLD_HOUSE:
                return "RTMSDataSvcRHRent/getRTMSDataSvcRHRent?";
            case FAMILY_HOUSE:
                return "RTMSDataSvcSHRent/getRTMSDataSvcSHRent?";
            default:
                throw new IllegalArgumentException("해당 주택유형의 실거래가 데이터조회는 지원하지 않습니다.: " + rentHousingType);
        }
    }

    public String getFullApiUrl(String districtCodeFirst5, String dealYmd, RentHousingType rentHousingType) throws IOException {
        String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
        return endPointUrl
                + endpointMethod(rentHousingType)
                + "LAWD_CD=" + URLEncoder.encode(districtCodeFirst5, "UTF-8")
                + "&DEAL_YMD=" + URLEncoder.encode(dealYmd, "UTF-8")
                + "&numOfRows=" + "10000"
                + "&serviceKey=" + encodedServiceKey;


    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();  // RestTemplate 객체를 생성하고 빈으로 등록
    }

}
