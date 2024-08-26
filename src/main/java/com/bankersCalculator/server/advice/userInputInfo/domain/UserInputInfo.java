package com.bankersCalculator.server.advice.userInputInfo.domain;

import com.bankersCalculator.server.advice.loanAdvice.dto.request.LoanAdviceServiceRequest;
import com.bankersCalculator.server.advice.loanAdvice.entity.LoanAdviceResult;
import com.bankersCalculator.server.common.enums.loanAdvice.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvice.MaritalStatus;
import com.bankersCalculator.server.housingInfo.rentTransactionInquiry.common.RentHousingType;
import com.bankersCalculator.server.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "userInputInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LoanAdviceResult loanAdviceResult;

    // 고객 정보
    private Long rentalDeposit;  // 임차보증금
    private Long monthlyRent;    // 월세
    private Long cashOnHand;     // 보유현금
    private Integer age;             // 만나이

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;  // 혼인상태

    private Long annualIncome;        // 연소득
    private Long spouseAnnualIncome;  // 배우자연소득

    @Enumerated(EnumType.STRING)
    private ChildStatus childStatus;  // 자녀상태

    private Boolean hasNewborn;       // 신생아여부
    private Boolean isSMEEmployee;      // 중소기업재직여부
    private Boolean isNetAssetOver345M;  // 순자산 3.45억 초과 여부

    // 주택정보
    @Enumerated(EnumType.STRING)
    private RentHousingType rentHousingType;  // 주택타입

    private Double exclusiveArea;  // 전용면적
    private String buildingName; // 건물명
    private String districtCode; // 법정동 코드
    private String dongName;     // 읍명동이름
    private String jibun;        // 지번

    public static UserInputInfo create(User user, LoanAdviceServiceRequest request) {
        return UserInputInfo.builder()
            .user(user)
            .rentalDeposit(request.getRentalDeposit().longValue())
            .monthlyRent(request.getMonthlyRent().longValue())
            .cashOnHand(request.getCashOnHand().longValue())
            .age(request.getAge())
            .maritalStatus(request.getMaritalStatus())
            .annualIncome(request.getAnnualIncome().longValue())
            .spouseAnnualIncome(request.getSpouseAnnualIncome().longValue())
            .childStatus(request.getChildStatus())
            .hasNewborn(request.getHasNewborn())
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