package com.bankersCalculator.server.housingInfo.service;

import com.bankersCalculator.server.housingInfo.buildingInfo.api.HousingTypeAndExclusiveAreaApiClient;
import com.bankersCalculator.server.housingInfo.buildingInfo.dto.HousingTypeAndExclusiveAreaApiResponse;
import com.bankersCalculator.server.housingInfo.dto.HousingInfoApiResponse;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.dto.RentTransactionInquiryResponse;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.service.RentTransactionInquiryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HousingInfoApiService {

    private final HousingTypeAndExclusiveAreaApiClient housingTypeApiClient;
    private final RentTransactionInquiryService rentTransactionInquiryService;

    public HousingInfoApiService(HousingTypeAndExclusiveAreaApiClient housingTypeApiClient,
                                 RentTransactionInquiryService rentTransactionInquiryService) {
        this.housingTypeApiClient = housingTypeApiClient;
        this.rentTransactionInquiryService = rentTransactionInquiryService;
    }

    public List<HousingInfoApiResponse> getHousingInfo(String districtCode, String jibun, String dongName) throws IOException {
        // Step 1: districtCode와 jibun을 분리
        String districtCodeFirst5 = districtCode.substring(0, 5);
        String districtCodeLast5 = districtCode.substring(5);
        String[] parsedJibun = parseJibun(jibun);

        String jibunMain = parsedJibun[0];
        String jibunSub = parsedJibun[1];

        // Step 2: 주택 유형 및 전용 면적 정보
        HousingTypeAndExclusiveAreaApiResponse housingTypeInfo = housingTypeApiClient.getApHsTpInfo(
                districtCodeFirst5, districtCodeLast5, jibunMain, jibunSub);

        List<HousingInfoApiResponse> result = new ArrayList<>();

        // Step 3: housingTypeInfo에서 필요한 정보를 추출하여 리스트에 추가
        for (HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem item : housingTypeInfo.getBody().getItems().getItemList()) {
            String rentHousingTypeName = item.getRentHousingTypeName();
            double exclusiveArea = item.getExclusiveArea();
            int rentHousingTypeCode = Integer.parseInt(item.getRentHousingTypeCode());

            // RentHousingType 매핑
            RentHousingType rentHousingType;
            switch (rentHousingTypeCode) {
                case 1:
                    rentHousingType = RentHousingType.APARTMENT;
                    break;
                case 2:
                    rentHousingType = RentHousingType.OFFICETEL;
                    break;
                case 3:
                case 4:
                    rentHousingType = RentHousingType.HOUSEHOLD_HOUSE;
                    break;
                case 5:
                case 6:
                    rentHousingType = RentHousingType.FAMILY_HOUSE;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Rent Housing Type Code: " + rentHousingTypeCode);
            }

            // Step 4: 임대 거래 데이터
            RentTransactionInquiryResponse rentTransactionResponse = rentTransactionInquiryService.getRentTransactionsResult(
                    districtCodeFirst5, rentHousingType, 3, dongName, jibun);

            // Step 5: 평균 보증금과 거래 건수를 추출하고, 정보를 활용
            for (Map.Entry<String, RentTransactionInquiryResponse.AverageInfo> entry : rentTransactionResponse.getAverageInfoByExcluUseAr().entrySet()) {
                String excluUseAr = entry.getKey();
                RentTransactionInquiryResponse.AverageInfo avgInfo = entry.getValue();

                result.add(new HousingInfoApiResponse(
                        rentHousingTypeName,
                        Double.parseDouble(excluUseAr),
                        avgInfo.getAverageDeposit(),
                        avgInfo.getAverageMonthlyRent(),
                        avgInfo.getTransactionCount()
                ));
            }
        }

        return result;
    }

    public static String[] parseJibun(String jibun) {
        // 지번을 "-" 기준으로 분리
        String[] parts = jibun.split("-");

        // 메인 지번 (앞부분)
        String main = parts[0];
        main = String.format("%04d", Integer.parseInt(main)); // 4자리 맞추기, 앞에 0 채우기

        // 서브 지번 (뒷부분)
        String sub = (parts.length > 1) ? parts[1] : "0000";
        sub = String.format("%04d", Integer.parseInt(sub));   // 4자리 맞추기, 앞에 0 채우기

        return new String[]{main, sub};
    }
}
