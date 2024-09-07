package com.myZipPlan.server.housingInfo.service;

import com.myZipPlan.server.housingInfo.buildingInfo.api.HousingTypeAndExclusiveAreaApiClient;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.HousingTypeAndExclusiveAreaApiResponse;
import com.myZipPlan.server.housingInfo.buildingInfo.common.RentHousingType;
import com.myZipPlan.server.housingInfo.buildingInfo.dto.RentTransactionInquiryResponse;
import com.myZipPlan.server.housingInfo.buildingInfo.service.RentTransactionInquiryService;
import com.myZipPlan.server.housingInfo.dto.HousingInfoResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HousingInfoService {

    private final HousingTypeAndExclusiveAreaApiClient housingTypeAndExclusiveAreaApiClient;
    private final RentTransactionInquiryService rentTransactionInquiryService;

    public HousingInfoService(HousingTypeAndExclusiveAreaApiClient housingTypeApiClient,
                              RentTransactionInquiryService rentTransactionInquiryService) {
        this.housingTypeAndExclusiveAreaApiClient = housingTypeApiClient;
        this.rentTransactionInquiryService = rentTransactionInquiryService;
    }

    public Map<String, Object> getHousingInfo(String districtCode, String jibun, String dongName) throws IOException {
        // Step 1: districtCode와 jibun을 분리
        String districtCodeFirst5 = districtCode.substring(0, 5);
        String districtCodeLast5 = districtCode.substring(5);
        String[] parsedJibun = parseJibun(jibun);

        String jibunMain = parsedJibun[0];
        String jibunSub = parsedJibun[1];

        // Step 2: 주택 유형 및 전용 면적 정보
        Map<String, Object> housingTypeAndExclusiveAreaApiResponse = housingTypeAndExclusiveAreaApiClient.InquiryHousingTypeAndExclusiveArea(
                districtCodeFirst5, districtCodeLast5, jibunMain, jibunSub);

        String apiResultCode = (String)housingTypeAndExclusiveAreaApiResponse.get("apiResultCode");
        String apiResultMessage = (String)housingTypeAndExclusiveAreaApiResponse.get("apiResultMessage");

        Map<String, Object> housingInfoResult = new HashMap<>();
        housingInfoResult.put("apiResultCode", apiResultCode);
        housingInfoResult.put("apiResultMessage", apiResultMessage);

        if (apiResultCode.equals("Y")) {
            List<HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem> itemList = (List<HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem>) housingTypeAndExclusiveAreaApiResponse.get("housingTypeAndExclusiveAreaList");
            List<HousingInfoResponse> housingInfoList = new ArrayList<>();
            // Step 3: housingTypeInfo에서 필요한 정보를 추출하여 리스트에 추가
            for (HousingTypeAndExclusiveAreaApiResponse.ApiResponseItem item : itemList) {
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

                    housingInfoList.add(new HousingInfoResponse(
                            rentHousingTypeName,
                            Double.parseDouble(excluUseAr),
                            avgInfo.getAverageDeposit(),
                            avgInfo.getAverageMonthlyRent(),
                            avgInfo.getTransactionCount()
                    ));
                }
                housingInfoResult.put("housingInfoList", housingInfoList);
            }
        }
        return housingInfoResult;
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
