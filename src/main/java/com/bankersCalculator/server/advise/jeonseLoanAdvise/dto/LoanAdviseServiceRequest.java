package com.bankersCalculator.server.advise.jeonseLoanAdvise.dto;

import com.bankersCalculator.server.common.enums.loanAdvise.AreaSize;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.RentalType;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanAdviseServiceRequest {

    // 고객 정보
    private int age;    // 만나이
    private long annualIncome;  //연소득
    private MaritalStatus maritalStatus; // 혼인상태
    private boolean newlyWedding;   // 신혼여부
    private LocalDate weddingDate;   // 혼인(예정)일
    private long spouseAnnualIncome;    // 배우자연소득
    private long cashOnHand;    // 보유현금
    private ChildStatus childStatus;    // 자녀상태
    private boolean hasNewborn; // 신생아여부
    private boolean worksForSME; // 중소기업재직여부(SME: SmallMediumEnterprise);

    // 주택정보 수기 투입
    private HousingType housingType;    // 주택타입
    private AreaSize rentalArea;    // 임차전용면적
    private RegionType regionType; // 주택위치
    // 부동산실거래가 연동
    private String propertyName;   // 건물명
    private long individualRentalArea; // 임차전용면적
    // 임차비용
    private List<rentalCost> rentalCostList;


    // 선택 항목
    private long housingPrice;  // 주택가액
    private long priorDepositAndClaims; // 선순위임차보증금 or 선순위채권
    private boolean isNetAssetOver345M; // 순자산 3.45억 초과 여부



    private static class rentalCost {
        private RentalType rentalType; // 임차타입
        private long rentalDeposit; // 임차보증금
        private long monthlyRent;   // 월세
    }
}
