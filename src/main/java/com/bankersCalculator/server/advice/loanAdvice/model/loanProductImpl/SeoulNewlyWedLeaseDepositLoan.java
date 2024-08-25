package com.bankersCalculator.server.advice.loanAdvice.model.loanProductImpl;

import com.bankersCalculator.server.advice.loanAdvice.dto.api.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.dto.service.FilterProductResultDto;
import com.bankersCalculator.server.advice.loanAdvice.model.LoanProduct;
import com.bankersCalculator.server.common.enums.JeonseLoanProductType;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.common.enums.ltv.HouseOwnershipType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeoulNewlyWedLeaseDepositLoan implements LoanProduct {

    @Override
    public JeonseLoanProductType getProductType() {
        return JeonseLoanProductType.SEOUL_NEWLYWED_LEASE_DEPOSIT;
    }

    @Override
    public String getProperty() {
        return null;
    }

    @Override
    public FilterProductResultDto filtering(LoanAdviceServiceRequest request) {
        /*
          서울시신혼부부임차보증금대출
          검증할 목록
          1. 주거지 서울 여부
          2. 신혼부부 여부
          3. 부부합산소득 1.3억 초과 여부
          4. 무주택자 여부
          5. 임차보증금 7억 이하 여부
         */

        List<String> notEligibleReasons = new ArrayList<>();

        // 1. 주거지 서울 여부. 법정동코드 11 - 서울시
        if (!request.getDistrictCode().startsWith("11")) {
            notEligibleReasons.add("서울시 거주자만 가능합니다.");
        }

        // 2. 신혼부부 여부
        if (!isEligibleMaritalStatus(request.getMaritalStatus())) {
            notEligibleReasons.add("신혼부부만 가능합니다.");
        }

        // 3. 부부합산소득 1.3억 초과 여부
        if (request.getAnnualIncome() + request.getSpouseAnnualIncome() > 130000000) {
            notEligibleReasons.add("부부합산소득 1.3억 초과시 대출 불가능합니다.");
        }

        // 4. 무주택자 여부
        if (request.getHouseOwnershipType() != HouseOwnershipType.NO_HOUSE) {
            notEligibleReasons.add("무주택자만 가능합니다.");
        }

        // 5. 임차보증금 7억 이하 여부
        if (request.getRentalDeposit() > 700000000) {
            notEligibleReasons.add("임차보증금 7억 이하만 가능합니다.");
        }

        return FilterProductResultDto.builder()
            .productType(getProductType())
            .isEligible(notEligibleReasons.isEmpty())
            .notEligibleReasons(notEligibleReasons)
            .build();
    }


    @Override
    public double calculateLoanLimit() {
        return 0;
    }


    // 이자율 산출


    // 기타비용산출(보증요율, 인지세, 보증보험료 등)


    private boolean isEligibleMaritalStatus(MaritalStatus maritalStatus) {
        return maritalStatus == MaritalStatus.ENGAGED || maritalStatus == MaritalStatus.MARRIED;
    }
}
