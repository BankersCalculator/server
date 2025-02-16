package com.myZipPlan.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamelWithJsonPropertyDTO {
    @JsonProperty("ACCT_NO")
    private String acctNo;
    @JsonProperty("USER_NAME")
    private String userName;
    @JsonProperty("BALANCE")
    private Long balance;
    @JsonProperty("BANK_NAME")
    private String bankName;
    @JsonProperty("BRANCH_CODE")
    private String branchCode;
    @JsonProperty("CURRENCY")
    private String currency;
    @JsonProperty("ACCOUNT_TYPE")
    private String accountType;
    @JsonProperty("INTEREST_RATE")
    private Double interestRate;
    @JsonProperty("OPEN_DATE")
    private String openDate;
    @JsonProperty("LAST_TRANSACTION_DATE")
    private String lastTransactionDate;
    @JsonProperty("IS_ACTIVE")
    private Boolean isActive;
    @JsonProperty("OVERDRAFT_LIMIT")
    private Long overdraftLimit;
    @JsonProperty("CREDIT_SCORE")
    private Integer creditScore;
    @JsonProperty("DEBIT_CARD_NUMBER")
    private String debitCardNumber;
    @JsonProperty("CREDIT_CARD_NUMBER")
    private String creditCardNumber;
    @JsonProperty("MONTHLY_DEPOSIT")
    private Long monthlyDeposit;
    @JsonProperty("MONTHLY_WITHDRAWAL")
    private Long monthlyWithdrawal;
    @JsonProperty("YEARLY_INTEREST")
    private Double yearlyInterest;
    @JsonProperty("LOAN_AMOUNT")
    private Long loanAmount;
    @JsonProperty("LOAN_STATUS")
    private String loanStatus;
    @JsonProperty("TAX_RATE")
    private Double taxRate;
    @JsonProperty("INSURANCE_PLAN")
    private String insurancePlan;
    @JsonProperty("TRANSACTION_HISTORY")
    private String transactionHistory;

    // 추가 필드 77개 (AA ~ CY)
    @JsonProperty("FIELD_AA")
    private String fieldAa;
    @JsonProperty("FIELD_AB")
    private String fieldAb;
    @JsonProperty("FIELD_AC")
    private String fieldAc;
    @JsonProperty("FIELD_AD")
    private String fieldAd;
    @JsonProperty("FIELD_AE")
    private String fieldAe;
    @JsonProperty("FIELD_AF")
    private String fieldAf;
    @JsonProperty("FIELD_AG")
    private String fieldAg;
    @JsonProperty("FIELD_AH")
    private String fieldAh;
    @JsonProperty("FIELD_AI")
    private String fieldAi;
    @JsonProperty("FIELD_AJ")
    private String fieldAj;
    @JsonProperty("FIELD_AK")
    private String fieldAk;
    @JsonProperty("FIELD_AL")
    private String fieldAl;
    @JsonProperty("FIELD_AM")
    private String fieldAm;
    @JsonProperty("FIELD_AN")
    private String fieldAn;
    @JsonProperty("FIELD_AO")
    private String fieldAo;
    @JsonProperty("FIELD_AP")
    private String fieldAp;
    @JsonProperty("FIELD_AQ")
    private String fieldAq;
    @JsonProperty("FIELD_AR")
    private String fieldAr;
    @JsonProperty("FIELD_AS")
    private String fieldAs;
    @JsonProperty("FIELD_AT")
    private String fieldAt;
    @JsonProperty("FIELD_AU")
    private String fieldAu;
    @JsonProperty("FIELD_AV")
    private String fieldAv;
    @JsonProperty("FIELD_AW")
    private String fieldAw;
    @JsonProperty("FIELD_AX")
    private String fieldAx;
    @JsonProperty("FIELD_AY")
    private String fieldAy;
    @JsonProperty("FIELD_AZ")
    private String fieldAz;
    @JsonProperty("FIELD_BA")
    private String fieldBa;
    @JsonProperty("FIELD_BB")
    private String fieldBb;
    @JsonProperty("FIELD_BC")
    private String fieldBc;
    @JsonProperty("FIELD_BD")
    private String fieldBd;
    @JsonProperty("FIELD_BE")
    private String fieldBe;
    @JsonProperty("FIELD_BF")
    private String fieldBf;
    @JsonProperty("FIELD_BG")
    private String fieldBg;
    @JsonProperty("FIELD_BH")
    private String fieldBh;
    @JsonProperty("FIELD_BI")
    private String fieldBi;
    @JsonProperty("FIELD_BJ")
    private String fieldBj;
    @JsonProperty("FIELD_BK")
    private String fieldBk;
    @JsonProperty("FIELD_BL")
    private String fieldBl;
    @JsonProperty("FIELD_BM")
    private String fieldBm;
    @JsonProperty("FIELD_BN")
    private String fieldBn;
    @JsonProperty("FIELD_BO")
    private String fieldBo;
    @JsonProperty("FIELD_BP")
    private String fieldBp;
    @JsonProperty("FIELD_BQ")
    private String fieldBq;
    @JsonProperty("FIELD_BR")
    private String fieldBr;
    @JsonProperty("FIELD_BS")
    private String fieldBs;
    @JsonProperty("FIELD_BT")
    private String fieldBt;
    @JsonProperty("FIELD_BU")
    private String fieldBu;
    @JsonProperty("FIELD_BV")
    private String fieldBv;
    @JsonProperty("FIELD_BW")
    private String fieldBw;
    @JsonProperty("FIELD_BX")
    private String fieldBx;
    @JsonProperty("FIELD_BY")
    private String fieldBy;
    @JsonProperty("FIELD_BZ")
    private String fieldBz;
    @JsonProperty("FIELD_CA")
    private String fieldCa;
    @JsonProperty("FIELD_CB")
    private String fieldCb;
    @JsonProperty("FIELD_CC")
    private String fieldCc;
    @JsonProperty("FIELD_CD")
    private String fieldCd;
    @JsonProperty("FIELD_CE")
    private String fieldCe;
    @JsonProperty("FIELD_CF")
    private String fieldCf;
    @JsonProperty("FIELD_CG")
    private String fieldCg;
    @JsonProperty("FIELD_CH")
    private String fieldCh;
    @JsonProperty("FIELD_CI")
    private String fieldCi;
    @JsonProperty("FIELD_CJ")
    private String fieldCj;
    @JsonProperty("FIELD_CK")
    private String fieldCk;
    @JsonProperty("FIELD_CL")
    private String fieldCl;
    @JsonProperty("FIELD_CM")
    private String fieldCm;
    @JsonProperty("FIELD_CN")
    private String fieldCn;
    @JsonProperty("FIELD_CO")
    private String fieldCo;
    @JsonProperty("FIELD_CP")
    private String fieldCp;
    @JsonProperty("FIELD_CQ")
    private String fieldCq;
    @JsonProperty("FIELD_CR")
    private String fieldCr;
    @JsonProperty("FIELD_CS")
    private String fieldCs;
    @JsonProperty("FIELD_CT")
    private String fieldCt;
    @JsonProperty("FIELD_CU")
    private String fieldCu;
    @JsonProperty("FIELD_CV")
    private String fieldCv;
    @JsonProperty("FIELD_CW")
    private String fieldCw;
    @JsonProperty("FIELD_CX")
    private String fieldCx;
    @JsonProperty("FIELD_CY")
    private String fieldCy;
}