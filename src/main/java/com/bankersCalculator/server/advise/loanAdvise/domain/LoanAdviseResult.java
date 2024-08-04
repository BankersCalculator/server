package com.bankersCalculator.server.advise.loanAdvise.domain;

import com.bankersCalculator.server.common.enums.Bank;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class LoanAdviseResult {

    // TODO: 검토 및 수정할 것. alternativeProducts 를 굳이 저장해야하는지 등.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loanProductName;
    private String loanProductCode;
    private double possibleLoanLimit;
    private double expectedLoanRate;

    private long totalRentalDeposit;
    private long loanAmount;
    private long ownFunds;

    private long monthlyInterestCost;
    private long monthlyRent;
    private long totalLivingCost;

    private long opportunityCostOwnFunds;
    private double depositInterestRate;
    private long calculatedCost;

    private long guaranteeInsuranceFee;
    private long stampDuty;

    @Column(length = 4000)
    private String recommendationReason;

    @ElementCollection
    private List<AlternativeProduct> alternativeProducts;

    @ElementCollection(targetClass = Bank.class)
    @Enumerated(EnumType.STRING)
    private List<Bank> availableBanks;

    @Column(length = 4000)
    private String rentalLoanGuide;


    @Getter
    @Embeddable
    public static class AlternativeProduct {
        private String loanProductName;
        private String loanProductCode;
        private double possibleLoanLimit;
        private double expectedLoanRate;
        private String notEligibleReason;
    }

}