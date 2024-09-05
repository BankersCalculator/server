package com.myZipPlan.server.common.enums.loanAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JeonseLoanProductType {

    // 주택금융공사
    SEOUL_NEWLYWED_LEASE_LOAN("서울시신혼부부임차보증금대출", "HF-01"),
    SEOUL_YOUTH_LEASE_LOAN("서울시청년임차보증금대출", "HF-02"),
    HF_LEASE_LOAN("주택신보전세자금대출", "HF-03"),
    YOUTH_LEASE_LOAN("청년전세론", "HF-04"),
    SPECIAL_HOMELESS_YOUTH_LOAN("(특례)무주택청년", "HF-05"),
    SPECIAL_MULTI_CHILD_LOAN("(특례)다자녀가구", "HF-06"),
    FIXED_RATE_LEASE_LOAN("고정금리 협약전세자금보증", "HF-07"),

    // 주택도시기금
    NEWBORN_SPECIAL_LEASE_LOAN("신생아특례버팀목전세자금대출", "NHUF-01"),
    YOUTH_EXCLUSIVE_LEASE_LOAN("청년전용버팀목전세자금대출", "NHUF-02"),
    SME_YOUTH_MONTHLY_RENT_LOAN("중소기업취업청년전월세대출", "NHUF-03"),
    NEWLYWED_EXCLUSIVE_LEASE_LOAN("신혼부부전용전세자금대출", "NHUF-04"),
    BUTTRESS_LEASE_LOAN("버팀목전세자금", "NHUF-05"),

    // 서울보증보험
    QUALITY_HOUSING_LEASE_LOAN("우량주택전세론", "SGI-01"),

    // 도시보증공사
    HUG_LEASE_SAFETY_LOAN("전세안심대출", "HUG-01");


    private final String productName;
    private final String productCode;

}
