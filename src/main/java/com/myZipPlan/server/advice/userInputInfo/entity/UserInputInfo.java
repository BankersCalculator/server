package com.myZipPlan.server.advice.userInputInfo.entity;

import com.myZipPlan.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.myZipPlan.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.myZipPlan.server.common.domain.BaseTimeEntity;
import com.myZipPlan.server.common.enums.calculator.HouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.ChildStatus;
import com.myZipPlan.server.common.enums.loanAdvice.JeonseHouseOwnershipType;
import com.myZipPlan.server.common.enums.loanAdvice.MaritalStatus;
import com.myZipPlan.server.housingInfo.buildingInfo.common.RentHousingType;
import com.myZipPlan.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "user_input_info")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserInputInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "userInputInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LoanAdviceResult loanAdviceResult;

    // 고객 정보
    private BigDecimal rentalDeposit;  // 임차보증금
    private BigDecimal monthlyRent;    // 월세
    private BigDecimal cashOnHand;     // 보유현금
    private Integer age;             // 만나이

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;  // 혼인상태

    private BigDecimal annualIncome;        // 연소득
    private BigDecimal spouseAnnualIncome;  // 배우자연소득

    @Enumerated(EnumType.STRING)
    private ChildStatus childStatus;  // 자녀상태
    private Boolean hasNewborn;       // 신생아여부

    private JeonseHouseOwnershipType houseOwnershipType; // 주택소유형태

    private Boolean isSMEEmployee;      // 중소기업재직여부
    private Boolean isNetAssetOver345M;  // 순자산 3.45억 초과 여부

    // 주택정보
    @Enumerated(EnumType.STRING)
    private RentHousingType rentHousingType;  // 주택타입
    private BigDecimal exclusiveArea;  // 전용면적
    private String buildingName; // 건물명
    private String districtCode; // 법정동 코드
    private String dongName;     // 읍명동이름
    private String jibun;        // 지번

    public static UserInputInfo create(User user, LoanAdviceServiceRequest request) {
        return UserInputInfo.builder()
            .user(user)
            .rentalDeposit(request.getRentalDeposit())
            .monthlyRent(request.getMonthlyRent())
            .cashOnHand(request.getCashOnHand())
            .age(request.getAge())
            .maritalStatus(request.getMaritalStatus())
            .annualIncome(request.getAnnualIncome())
            .spouseAnnualIncome(request.getSpouseAnnualIncome())
            .childStatus(request.getChildStatus())
            .hasNewborn(request.getHasNewborn())
            .houseOwnershipType(request.getHouseOwnershipType())
            .isSMEEmployee(request.getIsSMEEmployee())
            .isNetAssetOver345M(request.getIsNetAssetOver345M())
            .rentHousingType(request.getRentHousingType())
            .exclusiveArea(request.getExclusiveArea())
            .buildingName(request.getBuildingName())
            .districtCode(request.getDistrictCode())
            .dongName(request.getDongName())
            .jibun(request.getJibun())
            .build();
    }

    public void setLoanAdviceResult(LoanAdviceResult loanAdviceResult) {
        this.loanAdviceResult = loanAdviceResult;
    }
}