package com.bankersCalculator.server.advise.loanAdvise.domain;

import com.bankersCalculator.server.common.enums.loanAdvise.AreaSize;
import com.bankersCalculator.server.common.enums.loanAdvise.ChildStatus;
import com.bankersCalculator.server.common.enums.loanAdvise.MaritalStatus;
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

    private int age;
    private long annualIncome;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private boolean newlyWedding;
    private LocalDate weddingDate;
    private long spouseAnnualIncome;
    private long cashOnHand;

    @Enumerated(EnumType.STRING)
    private ChildStatus childStatus;

    private boolean hasNewborn;
    private boolean worksForSME;

    @Enumerated(EnumType.STRING)
    private HousingType housingType;

    @Enumerated(EnumType.STRING)
    private AreaSize rentalArea;

    @Enumerated(EnumType.STRING)
    private RegionType regionType;

    private String propertyName;
    private long individualRentalArea;

    @OneToMany(mappedBy = "userInputInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentalCost> rentalCostList;

    private long housingPrice;
    private long priorDepositAndClaims;
    private boolean isNetAssetOver345M;
}