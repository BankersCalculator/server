package com.bankersCalculator.server.advise.jeonseLoanAdvise.dto;

import com.bankersCalculator.server.common.enums.loanAdvise.AreaSize;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanAdviseServiceRequest {

    // 고객 정보
    private int age;    // 만나이
    private MaritalStatus maritalStatus; // 혼인상태
    private boolean newlyMarried;   // 신혼여부
    private ChildStatus childStatus;    // 자녀상태
    private boolean hasNewborn; // 신생아여부
    private int numberOfHousesOwned;    // 주택보우슈
    private long annualIncome;  //연소득
    private long spouseAnnualIncome;    // 배우자연소득
    private long cashOnHand;    // 보유현금
    private boolean worksForSME; // 중소기업재직여부(SME: SmallMediumEnterprise);

    // 주택 정보
    private long rentalDeposit;
    private long monthlyRent;
    private HousingType housingType;
    private AreaSize rentalArea;
//    private Location location; // 흠... 지역을 어떻게 구분해야 하려나?

    // 부채 현황
//    private List<Loan> existingLoans; // 부채현황...




}
