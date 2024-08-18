package com.bankersCalculator.server.advise.loanAdvise.domain;

import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user_input_info")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInputInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 고객 정보
    private long rentalDeposit;  // 임차보증금
    private long monthlyRent;    // 월세
    private long cashOnHand;     // 보유현금
    private int age;             // 만나이

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;  // 혼인상태

    private long annualIncome;        // 연소득
    private long spouseAnnualIncome;  // 배우자연소득

    @Enumerated(EnumType.STRING)
    private ChildStatus childStatus;  // 자녀상태

    private boolean hasNewborn;       // 신생아여부
    private Boolean isSMEEmployee;      // 중소기업재직여부
    private Boolean isNetAssetOver345M;  // 순자산 3.45억 초과 여부

    // 주택정보
    @Enumerated(EnumType.STRING)
    private RentHousingType rentHousingType;  // 주택타입

    private long exclusiveArea;  // 전용면적
    private String buildingName; // 건물명
    private String districtCode; // 법정동 코드
    private String dongName;     // 읍명동이름
    private String jibun;        // 지번
}