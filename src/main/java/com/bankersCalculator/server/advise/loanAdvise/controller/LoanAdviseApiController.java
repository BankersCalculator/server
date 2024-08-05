package com.bankersCalculator.server.advise.loanAdvise.controller;

import com.bankersCalculator.server.advise.loanAdvise.domain.RentalCost;
import com.bankersCalculator.server.advise.loanAdvise.domain.UserInputInfo;
import com.bankersCalculator.server.advise.loanAdvise.dto.LoanAdviseRequest;
import com.bankersCalculator.server.advise.loanAdvise.dto.LoanAdviseResponse;
import com.bankersCalculator.server.advise.loanAdvise.dto.UserInputInfoRequest;
import com.bankersCalculator.server.advise.loanAdvise.dto.UserInputInfoResponse;
import com.bankersCalculator.server.advise.loanAdvise.service.LoanAdviseService;
import com.bankersCalculator.server.common.enums.loanAdvise.AreaSize;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.RentalType;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;

@RequiredArgsConstructor
@RequestMapping("/api/v1/loanAdvise")
@RestController
public class LoanAdviseApiController {

    private final LoanAdviseService loanAdviseService;

    @GetMapping("/userInfo")
    public UserInputInfoResponse getSubmittedUserInput(@RequestBody UserInputInfoRequest request) {
//        UserInputInfoResponse submittedUserInput = loanAdviseService.getSubmittedUserInput(request.toServiceRequest());

        return UserInputInfoResponse.builder()
            .age(30)
            .annualIncome(50000000L)
            .maritalStatus(MaritalStatus.MARRIED)
            .newlyWedding(true)
            .weddingDate(LocalDate.of(2023, 1, 1))
            .spouseAnnualIncome(40000000L)
            .cashOnHand(20000000L)
            .childStatus(ChildStatus.ONE_CHILD)
            .hasNewborn(true)
            .worksForSME(false)
            .housingType(HousingType.APARTMENT)
            .rentalArea(AreaSize.UNDER_85_SQM)
            .regionType(RegionType.SEOUL)
            .propertyName("Sample Apartment")
            .manualInputRentalArea(75L)
            .rentalCostList(Arrays.asList(
                RentalCost.builder()
                .id(1L)
                .rentalType(RentalType.JEONSE)
                    .rentalDeposit(300000000)
                    .monthlyRent(0)
                    .build(),
                RentalCost.builder()
                    .id(2L)
                    .rentalType(RentalType.WOLSE)
                    .rentalDeposit(100000000)
                    .monthlyRent(300000)
                    .build())
            )
            .housingPrice(300000000L)
            .priorDepositAndClaims(50000000L)
            .netAssetOver345M(false)
            .build();

    }

    @PostMapping
    public LoanAdviseResponse generateLoanAdvise(@RequestBody LoanAdviseRequest request) {
        loanAdviseService.generateLoanAdvise(request.toServiceRequest());
        return null;
    }

    @PostMapping("/{productCode}")
    public LoanAdviseResponse generateLoanAdviseOnSpecificLoan(@PathVariable String productCode,
                                                               @RequestBody LoanAdviseRequest request) {
        loanAdviseService.generateLoanAdviseOnSpecificLoan(productCode, request.toServiceRequest());
        return null;
    }


}
