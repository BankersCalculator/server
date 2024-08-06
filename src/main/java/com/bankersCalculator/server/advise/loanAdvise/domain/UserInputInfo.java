package com.bankersCalculator.server.advise.loanAdvise.domain;

import com.bankersCalculator.server.advise.loanAdvise.dto.RentalCostDto;
import com.bankersCalculator.server.common.enums.loanAdvise.AreaSize;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.UserType;
import com.bankersCalculator.server.common.enums.ltv.HousingType;
import com.bankersCalculator.server.common.enums.ltv.RegionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
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

    private UserType userType; //  회원/비회원
    private int age; // 만나이
    private long annualIncome; // 연소득
    private long cashOnHand; // 보유 현금

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus; // 결혼 상태
    private boolean newlyWedding; // 신혼 여부
    private LocalDate weddingDate; // 결혼 날짜
    private long spouseAnnualIncome; // 배우자 연소득

    @Enumerated(EnumType.STRING)
    private ChildStatus childStatus; // 자녀 상태
    private boolean hasNewborn; // 신생아 여부

    private boolean worksForSME; // 중소기업 근무 여부

    // 부동산 정보 수기투입
    @Enumerated(EnumType.STRING)
    private HousingType housingType; // 주택 유형
    @Enumerated(EnumType.STRING)
    private AreaSize rentalArea; // 임대 면적
    @Enumerated(EnumType.STRING)
    private RegionType regionType; // 지역 유형

    // 부동산실거래가 연동
    private String propertyName; // 부동산명
    private long manualInputRentalArea; // 수기투입 임차면적

    @OneToMany(mappedBy = "userInputInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentalCost> rentalCostList; // 임대 비용(전세/월세/반전세 등)

    // 선택입력 3종
    private long housingPrice; // 주택 가격
    private long priorDepositAndClaims; // 이전 보증금 및 청구액
    private boolean isNetAssetOver345M; // 순자산이 3억4500만원 초과 여부

    public List<RentalCostDto> getRentalCostDtoList() {
        return rentalCostList.stream()
            .map(ap -> RentalCostDto.builder()
                .rentalType(ap.getRentalType())
                .rentalDeposit(ap.getRentalDeposit())
                .monthlyRent(ap.getMonthlyRent())
                .build())
            .toList();
    }
}