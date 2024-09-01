package com.myZipPlan.server.common.message;

public abstract class ValidationMessage {

    public static final String NOT_NULL_REPAYMENT_TYPE = "상환 유형은 필수값입니다.";
    public static final String NOT_NULL_PRINCIPAL = "원금은 필수값입니다.";
    public static final String NOT_NULL_TERM = "대출 기간은 필수값입니다.";
    public static final String NOT_NULL_INTEREST_RATE_PERCENTAGE = "연이자율은 필수값입니다.";


    public static final String MIN_VALUE_PRINCIPAL = "원금은 1백만원 이상이어야 합니다.";
    public static final String MAX_VALUE_PRINCIPAL = "원금은  100억원을 초과할 수 없습니다.";
    public static final String MIN_VALUE_TERM = "대출 기간은 1개월 이상이어야 합니다.";
    public static final String MAX_VALUE_TERM = "대출 기간은 600개월을 초과할 수 없습니다.";
    public static final String MAX_VALUE_GRACE_PERIOD = "거치기간은 600개월을 초과할 수 없습니다.";
    public static final String MIN_VALUE_INTEREST_RATE_PERCENTAGE = "연이자율은 0보다 커야 합니다.";
    public static final String MAX_VALUE_INTEREST_RATE_PERCENTAGE = "연이자율은 20%를 초과할 수 없습니다.";


}
