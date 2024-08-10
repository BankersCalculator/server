package com.bankersCalculator.server.api.ltvCalc.service;

import com.bankersCalculator.server.calculator.ltvCalc.dto.LtvCalcResponse;
import com.bankersCalculator.server.calculator.ltvCalc.dto.LtvCalcServiceRequest;
import com.bankersCalculator.server.calculator.ltvCalc.service.LtvCalcService;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LtvCalcServiceTest {

    @Autowired
    private LtvCalcService ltvCalcService;

    private final double DELTA = 0.001;

    @DisplayName("LTV 정상 산출")
    @MethodSource("provideTestCases")
    @ParameterizedTest
    void calculateLtv(double loanAmount, double collateralValue,
                      double priorMortgage, int numberOfRooms,
                      HousingType housingType, RegionType regionType,
                      double currentLeaseDeposit, double expectedLtvRatio) throws Exception{
        //given
        LtvCalcServiceRequest request = createTestRequest(loanAmount, collateralValue, priorMortgage, numberOfRooms, housingType, regionType, currentLeaseDeposit);

        //when
        LtvCalcResponse response = ltvCalcService.ltvCalculate(request);

        //then
        assertCommonExpectations(response, request, expectedLtvRatio);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
            Arguments.of(300000000, 500000000, 0, 3, HousingType.APARTMENT, RegionType.SEOUL, 0, 0.71),
            Arguments.of(200000000, 450000000, 0, 3, HousingType.APARTMENT, RegionType.SEOUL, 0, 0.567),
            Arguments.of(200000000, 450000000, 0, 3, HousingType.DETACHED_HOUSE, RegionType.SEOUL, 0, 0.689),
            Arguments.of(200000000, 450000000, 0, 3, HousingType.MULTI_FAMILY_HOUSE, RegionType.SEOUL, 0, 0.689),
            Arguments.of(200000000, 450000000, 0, 3, HousingType.APARTMENT, RegionType.CAPITAL_AREA, 0, 0.551),
            Arguments.of(200000000, 450000000, 0, 3, HousingType.APARTMENT, RegionType.METROPOLITAN_CITY, 0, 0.507),
            Arguments.of(200000000, 450000000, 0, 3, HousingType.APARTMENT, RegionType.OTHER_AREAS, 0, 0.50)
            );
    }

    private LtvCalcServiceRequest createTestRequest(double loanAmount, double collateralValue,
                                                    double priorMortgage, int numberOfRooms,
                                                    HousingType housingType, RegionType regionType,
                                                    double currentLeaseDeposit) {
        return LtvCalcServiceRequest.builder()
            .loanAmount(loanAmount)
            .collateralValue(collateralValue)
            .priorMortgage(priorMortgage)
            .numberOfRooms(numberOfRooms)
            .housingType(housingType)
            .regionType(regionType)
            .currentLeaseDeposit(currentLeaseDeposit)
            .build();
    }

    private void assertCommonExpectations(LtvCalcResponse response, LtvCalcServiceRequest request, double expectedLtvRatio) {
        assertEquals(request.getLoanAmount(), response.getLoanAmount(), DELTA);
        assertEquals(request.getCollateralValue(), response.getCollateralValue(), DELTA);
        assertEquals(expectedLtvRatio, response.getLtvRatio(), DELTA);
    }

}