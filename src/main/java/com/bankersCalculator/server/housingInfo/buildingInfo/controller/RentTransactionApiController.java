package com.bankersCalculator.server.housingInfo.buildingInfo.controller;
import com.bankersCalculator.server.common.api.ApiResponse;
import com.bankersCalculator.server.housingInfo.buildingInfo.api.RentTransactionApiClient;
import com.bankersCalculator.server.housingInfo.buildingInfo.dto.RentTransactionApiRequest;
import com.bankersCalculator.server.housingInfo.buildingInfo.dto.RentTransactionApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/rentTransactionApi")
@RequiredArgsConstructor
public class RentTransactionApiController {
    private final RentTransactionApiClient rentTransactionApiClient;

    @PostMapping
    public ApiResponse<RentTransactionApiResponse> RentTransactionCallApi(@RequestBody RentTransactionApiRequest request) throws IOException {
        RentTransactionApiResponse rentTransactionApiResponse = rentTransactionApiClient.RentTransactionCallApi(request.getDistrictCodeFirst5()
                                                                                                                , request.getDealYmd()
                                                                                                                , request.getRentHousingType()
                                                                                                                );
       return ApiResponse.ok(rentTransactionApiResponse);
    }
}
